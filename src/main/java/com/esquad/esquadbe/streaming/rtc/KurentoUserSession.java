package com.esquad.esquadbe.streaming.rtc;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class KurentoUserSession {
    private static final Logger log = LoggerFactory.getLogger(KurentoUserSession.class);

    private final String userId;
    private final String nickname;
    private final WebSocketSession session;
    private final String roomId;
    private final Map<String, WebRtcEndpoint> webRtcEndpoints = new ConcurrentHashMap<>();
    private final Map<String, Boolean> sdpNegotiatedMap = new ConcurrentHashMap<>();
    private final List<IceCandidate> pendingIceCandidates = new ArrayList<>();
    private MediaPipeline pipeline;

    private final Queue<JsonObject> messageQueue = new ConcurrentLinkedQueue<>();

    public KurentoUserSession(String userId, String nickname, WebSocketSession session, String roomId) {
        this.userId = userId;
        this.nickname = nickname;
        this.session = session;
        this.roomId = roomId;
    }

    public void setPipeline(MediaPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void close() {
        for (WebRtcEndpoint webRtcEndpoint : webRtcEndpoints.values()) {
            if (webRtcEndpoint != null) {
                webRtcEndpoint.release();
            }
        }
        webRtcEndpoints.clear();
    }

    public String generateSdpOffer(String senderId) {
        try {
            WebRtcEndpoint webRtcEndpoint = getWebRtcEndpoint(senderId);
            if (webRtcEndpoint == null) {
                log.info("WebRtcEndpoint가 없음. 사용자 '{}'에 대해 새로 생성 중...", senderId);
                webRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();
                addIceCandidateListener(senderId, webRtcEndpoint);
                setWebRtcEndpoint(senderId, webRtcEndpoint);
            }

            String sdpOffer = webRtcEndpoint.generateOffer();
            log.info("SDP Offer 생성 완료: 사용자 '{}', SDP Offer: {}", senderId, sdpOffer);
            return sdpOffer;
        } catch (Exception e) {
            log.error("SDP Offer 생성 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }


    public void sendExistingParticipantInfoToNewUser(KurentoUserSession existingUser) {
        JsonObject message = new JsonObject();
        message.addProperty("id", "existingParticipant");
        message.addProperty("userId", existingUser.getUserId());
        message.addProperty("nickname", existingUser.getNickname());

        sendMessage(message);
    }

    public void sendNewParticipantInfoToExistingUser(KurentoUserSession newUser) {
        JsonObject message = new JsonObject();
        message.addProperty("id", "newParticipant");
        message.addProperty("userId", newUser.getUserId());
        message.addProperty("nickname", newUser.getNickname());

        sendMessage(message);
    }

    private void sendMessage(JsonObject message) {
        synchronized (messageQueue) {
            messageQueue.add(message);
            if (messageQueue.size() == 1) {
                processMessageQueue();
            }
        }
    }

    private synchronized void processMessageQueue() {
        while (!messageQueue.isEmpty()) {
            JsonObject message = messageQueue.poll();
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message.toString()));
                } else {
                    log.warn("WebSocket 세션이 닫혀있어 메시지 전송 불가: 사용자 ID '{}'", this.userId);
                    messageQueue.add(message);
                    break;
                }
            } catch (IOException e) {
                log.error("WebSocket 메시지 전송 중 오류 발생: 사용자 ID '{}'", this.userId, e);
                messageQueue.add(message);
                break;
            } catch (IllegalStateException e) {
                log.error("WebSocket 세션 상태 오류: 사용자 ID '{}', 세션 상태: {}", this.userId, session.getTextMessageSizeLimit(), e);
                messageQueue.add(message);
                break;
            }
        }
    }

    public void receiveVideoFrom(KurentoUserSession sender, String sdpOffer) {
        try {
            String senderId = sender.getUserId();
            log.info("사용자 '{}'로부터 비디오 수신 중", senderId);

            // 이미 협상된 상태인 경우, 새로 SDP Offer 처리하지 않음
            if (isSdpNegotiated(senderId)) {
                log.warn("사용자 '{}'와 이미 협상이 완료된 상태입니다. SDP Offer 처리를 건너뜁니다.", senderId);
                return;
            }

            // 새로운 WebRtcEndpoint 생성
            log.info("WebRtcEndpoint가 없음. 사용자 '{}'에 대해 새로 생성 중", senderId);
            WebRtcEndpoint webRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();
            addIceCandidateListener(senderId, webRtcEndpoint);
            setWebRtcEndpoint(senderId, webRtcEndpoint);
            log.info("WebRtcEndpoint 생성 완료: {}", webRtcEndpoint);

            log.info("SDP Offer 처리 중. 사용자 '{}'", senderId);
            String sdpAnswer = webRtcEndpoint.processOffer(sdpOffer);
            log.info("SDP Answer 생성 완료: 사용자 '{}', SDP Answer: {}", senderId, sdpAnswer);

            webRtcEndpoint.gatherCandidates();
            log.info("ICE 후보자 수집 시작: 사용자 '{}'", senderId);

            setSdpNegotiated(senderId, true); // 협상 완료 상태 설정
            log.info("SDP 협상 완료: 사용자 '{}'", senderId);

            JsonObject response = new JsonObject();
            response.addProperty("id", "receiveVideoAnswer");
            response.addProperty("sdpAnswer", sdpAnswer);
            sendMessage(response);

            if (!pendingIceCandidates.isEmpty()) {
                log.info("준비된 WebRtcEndpoint에 대기 중이던 ICE 후보자 추가 중: 사용자 '{}'", senderId);
                for (IceCandidate candidate : pendingIceCandidates) {
                    webRtcEndpoint.addIceCandidate(candidate);
                }
                pendingIceCandidates.clear();
                log.info("ICE 후보자 추가 완료: 사용자 '{}'", senderId);
            }

        } catch (Exception e) {
            log.error("사용자로부터 비디오 수신 중 오류 발생: 사용자 '{}', 오류: {}", sender.getUserId(), e.getMessage(), e);
        }
    }

    private void addIceCandidateListener(String senderId, WebRtcEndpoint webRtcEndpoint) {
        if (webRtcEndpoint != null) {
            webRtcEndpoint.addIceCandidateFoundListener(event -> {
                JsonObject response = new JsonObject();
                response.addProperty("id", "iceCandidate");
                response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                sendMessage(response);
            });
        }
    }

    public boolean isSdpNegotiated(String senderId) {
        return sdpNegotiatedMap.getOrDefault(senderId, false);
    }

    public void setSdpNegotiated(String senderId, boolean negotiated) {
        sdpNegotiatedMap.put(senderId, negotiated);
    }

    public WebRtcEndpoint getWebRtcEndpoint(String senderId) {
        return webRtcEndpoints.get(senderId);
    }

    public void setWebRtcEndpoint(String senderId, WebRtcEndpoint webRtcEndpoint) {
        webRtcEndpoints.put(senderId, webRtcEndpoint);
    }

}
