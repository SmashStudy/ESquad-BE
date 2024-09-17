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


        } catch (Exception e) {
            log.error("세션 '{}'에서 방 참여 처리 중 오류 발생", session.getId(), e);
            sendErrorMessage(session, "방 참여 처리 중 오류 발생");
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
