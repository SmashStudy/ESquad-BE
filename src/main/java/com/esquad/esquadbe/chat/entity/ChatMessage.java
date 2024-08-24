package com.esquad.esquadbe.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String roomId;
    private String userId;
    private String message;
    private String type;
    private String messageId;
    private String status;
    private long timestamp;
}
