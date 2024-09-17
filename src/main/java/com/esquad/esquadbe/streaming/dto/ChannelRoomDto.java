package com.esquad.esquadbe.streaming.dto;

import lombok.Getter;

@Getter
public class ChannelRoomDto {
    private String roomId;

    public ChannelRoomDto(String roomId) {
        this.roomId = roomId;
    }
}