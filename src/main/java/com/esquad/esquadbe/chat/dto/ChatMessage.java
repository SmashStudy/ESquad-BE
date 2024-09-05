package com.esquad.esquadbe.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String roomId;
    private String userId;
    private String message;
    private String type;
    private String messageId;
    private String status;
    private LocalDate timestamp;
}
