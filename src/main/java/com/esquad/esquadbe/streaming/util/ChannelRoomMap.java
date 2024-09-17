package com.esquad.esquadbe.streaming.util;

import com.esquad.esquadbe.streaming.dto.KurentoRoomDto;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelRoomMap {

    private static ChannelRoomMap instance = new ChannelRoomMap();
    private final ConcurrentHashMap<String, KurentoRoomDto> channelRooms = new ConcurrentHashMap<>();

    private ChannelRoomMap() {}

    public static ChannelRoomMap getInstance() {
        return instance;
    }

    public ConcurrentHashMap<String, KurentoRoomDto> getChannelRooms() {
        return channelRooms;
    }
}
