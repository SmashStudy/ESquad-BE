package com.esquad.esquadbe.streaming.service;

import com.esquad.esquadbe.streaming.dto.KurentoRoomDto;
import com.esquad.esquadbe.streaming.rtc.KurentoUserSession;
import com.esquad.esquadbe.streaming.util.ChannelRoomMap;
import com.google.gson.JsonObject;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class KurentoManager {

    private final Logger log = LoggerFactory.getLogger(KurentoManager.class);
    private final ChannelRoomMap roomMap = ChannelRoomMap.getInstance();
    private static final int MAX_PARTICIPANTS = 12;
    private final Lock wsLock = new ReentrantLock();

    public KurentoRoomDto getRoom(String roomId) {
        return roomMap.getChannelRooms().get(roomId);
    }

    public synchronized KurentoRoomDto createRoom(String roomId, KurentoClient kurentoClient) {
        KurentoRoomDto room = new KurentoRoomDto(roomId, kurentoClient);
        roomMap.getChannelRooms().put(roomId, room);
        log.info("방 {} 생성됨", roomId);
        return room;
    }

    public synchronized void removeRoom(KurentoRoomDto room) {
        try {
            roomMap.getChannelRooms().remove(room.getRoomId());
            room.close();
            log.info("방 {} 삭제 및 종료됨", room.getRoomId());
        } catch (Exception e) {
            log.error("방 {} 삭제 중 오류 발생: {}", room.getRoomId(), e.getMessage());
        }
    }

    public synchronized void joinRoom(KurentoRoomDto room, KurentoUserSession user) {
        try {
            if (room.getParticipants().size() >= MAX_PARTICIPANTS) {
                log.warn("방 {} 가득 참, 사용자 {} 입장 불가", room.getRoomId(), user.getUserId());
                throw new RuntimeException("방이 가득 찼습니다. 최대 " + MAX_PARTICIPANTS + "명까지만 입장 가능합니다.");
            }

            room.getParticipants().put(user.getUserId(), user);
            user.setPipeline(room.getPipeline());
            log.info("사용자 {} 방 {} 에 참여함", user.getUserId(), room.getRoomId());

            room.broadcastNewParticipant(user.getUserId(), user.getNickname());

            for (KurentoUserSession participant : room.getParticipants().values()) {
                if (!participant.getUserId().equals(user.getUserId())) {

                    participant.sendNewParticipantInfoToExistingUser(user);

                    user.sendExistingParticipantInfoToNewUser(participant);

                    String offerSdp = participant.generateSdpOffer(user.getUserId());
                    user.receiveVideoFrom(participant, offerSdp);
                }
            }

            broadcastParticipantList(room);

        } catch (Exception e) {
            log.error("사용자 {} 의 방 {} 참여 처리 중 오류 발생: {}", user.getUserId(), room.getRoomId(), e.getMessage());
        }
    }

    private void broadcastParticipantList(KurentoRoomDto room) {
        wsLock.lock();
        try {
            log.info("방 '{}'의 참가자 목록을 브로드캐스트 중입니다.", room.getRoomId());

            JsonObject updateMessage = new JsonObject();
            updateMessage.addProperty("id", "updateParticipantList");
            JsonObject participants = new JsonObject();

            for (KurentoUserSession participant : room.getParticipants().values()) {
                participants.addProperty(participant.getUserId(), participant.getNickname());
            }

            updateMessage.add("participants", participants);

            for (KurentoUserSession participant : room.getParticipants().values()) {
                try {
                    sendSafeMessage(participant.getSession(), new TextMessage(updateMessage.toString()));
                } catch (Exception e) {
                    log.error("사용자 '{}'에게 참가자 목록 메시지 전송 중 오류 발생: {} - 메시지 내용: {}", participant.getUserId(), e.getMessage(), updateMessage.toString(), e);
                    sendErrorMessage(participant.getSession(), "참가자 목록 브로드캐스트 중 오류 발생");
                }
            }

            log.info("방 '{}'의 참가자 목록 브로드캐스트가 완료되었습니다.", room.getRoomId());
        } catch (Exception e) {
            log.error("방 '{}'의 참가자 목록 브로드캐스트 중 오류 발생", room.getRoomId(), e);
        } finally {
            wsLock.unlock();
        }
    }

    private void sendSafeMessage(WebSocketSession session, TextMessage message) {
        if (session.isOpen()) {
            try {
                if (session instanceof StandardWebSocketSession) {
                    StandardWebSocketSession standardSession = (StandardWebSocketSession) session;
                    if (standardSession.getNativeSession().isOpen()) {
                        sendMessageWithRetry(standardSession, message);
                    } else {
                        log.warn("세션 '{}'의 네이티브 세션이 닫혀 있어 메시지를 보낼 수 없습니다.", session.getId());
                    }
                } else {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                log.error("세션 '{}'에 메시지를 보내는 중 오류 발생: {}", session.getId(), e.getMessage(), e);
            }
        } else {
            log.warn("세션 '{}'이(가) 닫혀 있어 메시지를 보낼 수 없습니다.", session.getId());
        }
    }

    private void sendErrorMessage(WebSocketSession session, String error) {
        if (session.isOpen()) {
            try {
                JsonObject errorMessage = new JsonObject();
                errorMessage.addProperty("id", "error");
                errorMessage.addProperty("message", error);
                session.sendMessage(new TextMessage(errorMessage.toString()));
            } catch (IOException e) {
                log.error("세션 '{}'에 에러 메시지 전송 중 오류 발생: {}", session.getId(), e.getMessage(), e);
            }
        } else {
            log.warn("닫힌 세션 '{}'에 에러 메시지를 보내려고 했습니다.", session.getId());
        }
    }

    private void sendMessageWithRetry(StandardWebSocketSession session, TextMessage message) throws IOException {
        try {
            session.sendMessage(message);
        } catch (IllegalStateException e) {
            log.warn("세션 '{}'의 상태가 TEXT_PARTIAL_WRITING이므로 대기 후 재시도합니다.", session.getId());
            try {
                Thread.sleep(100);
                session.sendMessage(message);
            } catch (InterruptedException | IOException ex) {
                log.error("세션 '{}'에 메시지를 재전송하는 중 오류 발생: {}", session.getId(), ex.getMessage(), ex);
            }
        }
    }
}
