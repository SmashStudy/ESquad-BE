package com.esquad.esquadbe.streaming.dto;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import com.esquad.esquadbe.streaming.rtc.KurentoUserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.ConcurrentHashMap;

@Getter
public class KurentoRoomDto {

    private static final Logger log = LoggerFactory.getLogger(KurentoRoomDto.class);

    private final String roomId;
    private final MediaPipeline pipeline;
    private final ConcurrentHashMap<String, KurentoUserSession> participants = new ConcurrentHashMap<>();

    public KurentoRoomDto(String roomId, KurentoClient kurentoClient) {
        this.roomId = roomId;
        this.pipeline = kurentoClient.createMediaPipeline();
    }

    public void broadcastMessage(JsonObject message) {
        participants.values().forEach(user -> {
            try {
                user.getSession().sendMessage(new TextMessage(message.toString()));
            } catch (Exception e) {
                log.error("브로드캐스팅 중 오류 발생: {}", e.getMessage(), e);
            }
        });
    }

    public void broadcastNewParticipant(String userId, String nickname) {
        JsonObject newParticipantMessage = new JsonObject();
        newParticipantMessage.addProperty("id", "newParticipantArrived");
        newParticipantMessage.addProperty("userId", userId);
        newParticipantMessage.addProperty("nickname", nickname);

        broadcastMessage(newParticipantMessage);
    }

    public void close() {
        if (pipeline != null) {
            pipeline.release();
        }
        participants.clear();
    }

}
