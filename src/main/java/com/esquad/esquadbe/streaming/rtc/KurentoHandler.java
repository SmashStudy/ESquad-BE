package com.esquad.esquadbe.streaming.rtc;

import com.esquad.esquadbe.streaming.dto.KurentoRoomDto;
import com.esquad.esquadbe.streaming.service.KurentoManager;
import com.esquad.esquadbe.streaming.service.KurentoUserRegistry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class KurentoHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(KurentoHandler.class);
    private final KurentoUserRegistry registry;
    private final KurentoManager roomManager;
    private final KurentoClient kurentoClient;
    private final Gson gson = new Gson();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
            String id = jsonMessage.get("id").getAsString();

            KurentoUserSession user = registry.getBySession(session);
            if (user != null) {
                log.debug("사용자 '{}'로부터 메시지 수신: {}", user.getUserId(), jsonMessage);
            } else {
                log.debug("새로운 사용자로부터 메시지 수신: {}", jsonMessage);
            }

            switch (id) {
                case "joinRoom":
                    handleJoinRoom(session, jsonMessage);
                    break;
                case "leaveRoom":
                    handleLeaveRoom(session);
                    break;
                default:
                    log.warn("세션 '{}'에서 처리되지 않은 메시지 ID: {}", session.getId(), id);
                    break;
            }
        } catch (Exception e) {
            log.error("세션 '{}'에서 WebSocket 메시지 처리 중 오류 발생", session.getId(), e);
            sendErrorMessage(session, "메시지 처리 중 오류 발생");
        }
    }

    private void handleJoinRoom(WebSocketSession session, JsonObject jsonMessage) {
        try {
            log.info("세션 '{}'에서 'joinRoom' 처리 중", session.getId());

            String roomId = jsonMessage.get("roomID").getAsString();
            String userId = jsonMessage.get("userId").getAsString();
            String nickname = jsonMessage.get("nickname").getAsString();

            KurentoRoomDto room = getOrCreateRoom(roomId);
            KurentoUserSession user = createUserSession(session, userId, nickname, roomId, room);

            sendExistingParticipantInfo(room, user);

            exchangeIceCandidates(room, user);

        } catch (Exception e) {
            log.error("세션 '{}'에서 방 참여 처리 중 오류 발생", session.getId(), e);
            sendErrorMessage(session, "방 참여 처리 중 오류 발생");
        }
    }

    private void handleLeaveRoom(WebSocketSession session) {
        try {
            log.info("세션 '{}'에서 사용자가 방을 떠나는 중", session.getId());

            KurentoUserSession user = registry.removeBySession(session);
            if (user != null) {
                roomManager.leaveRoom(user);
                log.info("사용자 '{}'가 방 '{}'을 떠남", user.getUserId(), user.getRoomId());


            } else {
                log.warn("세션 '{}'에서 사용자를 찾을 수 없음", session.getId());
            }
        } catch (Exception e) {
            log.error("세션 '{}'에서 방 나가기 처리 중 오류 발생", session.getId(), e);
            sendErrorMessage(session, "방 나가기 처리 중 오류 발생");
        }
    }

    private void exchangeIceCandidates(KurentoRoomDto room, KurentoUserSession user) {
        for (KurentoUserSession participant : room.getParticipants().values()) {
            if (!participant.getUserId().equals(user.getUserId())) {
                try {
                    participant.sendIceCandidates(user, user.getUserId());
                    user.sendIceCandidates(participant, participant.getUserId());
                } catch (Exception e) {
                    log.error("ICE 후보자 교환 중 오류 발생: {}", e.getMessage(), e);
                }
            }
        }
    }

    private void sendExistingParticipantInfo(KurentoRoomDto room, KurentoUserSession user) {
        for (KurentoUserSession participant : room.getParticipants().values()) {
            if (!participant.getUserId().equals(user.getUserId())) {
                try {
                    participant.sendExistingParticipantInfoToNewUser(user);
                    user.sendExistingParticipantInfoToNewUser(participant);

                    if (!participant.isSdpNegotiated(user.getUserId())) {
                        String sdpOffer = participant.generateSdpOffer(user.getUserId());
                        if (sdpOffer != null) {
                            JsonObject receiveVideoMessage = new JsonObject();
                            receiveVideoMessage.addProperty("sender", participant.getUserId());
                            receiveVideoMessage.addProperty("sdpOffer", sdpOffer);
                            handleReceiveVideoFrom(user.getSession(), receiveVideoMessage);
                        } else {
                            log.warn("SDP Offer 생성 실패: 사용자 '{}'", participant.getUserId());
                        }
                    }
                } catch (Exception e) {
                    log.error("참가자 정보 전달 중 오류 발생: {}", e.getMessage(), e);
                }
            }
        }
    }

    private void handleReceiveVideoFrom(WebSocketSession session, JsonObject jsonMessage) {
        try {
            log.info("세션 '{}'에서 'receiveVideoFrom' 처리 중", session.getId());

            // 'sender'와 'sdpOffer' 필드에 대한 유효성 검사
            if (jsonMessage.has("sender") && !jsonMessage.get("sender").isJsonNull() &&
                    jsonMessage.has("sdpOffer") && !jsonMessage.get("sdpOffer").isJsonNull() &&
                    !jsonMessage.get("sdpOffer").getAsString().isEmpty()) {

                String senderName = jsonMessage.get("sender").getAsString();
                String sdpOffer = jsonMessage.get("sdpOffer").getAsString();

                KurentoUserSession sender = registry.getByName(senderName);
                KurentoUserSession user = registry.getBySession(session);

                if (sender != null && user != null) {
                    // SDP Offer 중복 처리를 방지
                    if (!user.isSdpNegotiated(senderName)) {
                        user.receiveVideoFrom(sender, sdpOffer);
                    } else {
                        log.warn("사용자 '{}'와 이미 협상이 완료된 상태입니다.", senderName);
                    }
                } else {
                    log.warn("세션 '{}'에서 송신자 또는 사용자를 찾을 수 없음", session.getId());
                    sendErrorMessage(session, "송신자 또는 사용자를 찾을 수 없음");
                }
            } else {
                log.warn("메시지에 'sender' 또는 'sdpOffer' 필드가 누락되었거나 유효하지 않음: {}", jsonMessage);
                sendErrorMessage(session, "필수 필드 누락 또는 유효하지 않음: sender 또는 sdpOffer");
            }
        } catch (Exception e) {
            log.error("세션 '{}'에서 SDP 제안 처리 중 오류 발생", session.getId(), e);
            sendErrorMessage(session, "SDP 제안 처리 중 오류 발생");
        }
    }

    private KurentoRoomDto getOrCreateRoom(String roomId) {
        KurentoRoomDto room = roomManager.getRoom(roomId);
        if (room == null) {
            log.info("새로운 방 생성 중: 방 ID '{}'", roomId);
            room = roomManager.createRoom(roomId, kurentoClient);
        }
        return room;
    }

    private KurentoUserSession createUserSession(WebSocketSession session, String userId, String nickname, String roomId, KurentoRoomDto room) {
        KurentoUserSession user = new KurentoUserSession(userId, nickname, session, roomId);
        user.setPipeline(room.getPipeline());
        roomManager.joinRoom(room, user);
        registry.register(user);
        log.info("사용자 '{}'가 방 '{}'에 참여함", userId, roomId);
        return user;
    }

    private void sendErrorMessage(WebSocketSession session, String error) {
        try {
            JsonObject errorMessage = new JsonObject();
            errorMessage.addProperty("id", "error");
            errorMessage.addProperty("message", error);
            session.sendMessage(new TextMessage(errorMessage.toString()));
        } catch (Exception e) {
            log.error("세션 '{}'에 에러 메시지 전송 중 오류 발생: {}", session.getId(), e.getMessage());
        }
    }
}
