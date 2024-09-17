package com.esquad.esquadbe.streaming.service;

import com.esquad.esquadbe.streaming.dto.KurentoRoomDto;
import com.esquad.esquadbe.streaming.rtc.KurentoUserSession;
import com.esquad.esquadbe.streaming.util.ChannelRoomMap;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KurentoManager {

    private final Logger log = LoggerFactory.getLogger(KurentoManager.class);
    private final ChannelRoomMap roomMap = ChannelRoomMap.getInstance();
    private static final int MAX_PARTICIPANTS = 12;

    public KurentoRoomDto getRoom(String roomId) {
        return roomMap.getChannelRooms().get(roomId);
    }

    public synchronized KurentoRoomDto createRoom(String roomId, KurentoClient kurentoClient) {
        KurentoRoomDto room = new KurentoRoomDto(roomId, kurentoClient);
        roomMap.getChannelRooms().put(roomId, room);
        log.info("방 {} 생성됨", roomId);
        return room;
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

        } catch (Exception e) {
            log.error("사용자 {} 의 방 {} 참여 처리 중 오류 발생: {}", user.getUserId(), room.getRoomId(), e.getMessage());
        }
    }

}
