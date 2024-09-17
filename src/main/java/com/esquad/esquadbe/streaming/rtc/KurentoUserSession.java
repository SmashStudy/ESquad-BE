package com.esquad.esquadbe.streaming.rtc;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
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
}
