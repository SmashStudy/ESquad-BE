package com.esquad.esquadbe.streaming.service;

import com.esquad.esquadbe.streaming.dto.KurentoRoomDto;
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
}
