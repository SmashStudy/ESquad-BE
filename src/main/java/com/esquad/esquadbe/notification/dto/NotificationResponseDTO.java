package com.esquad.esquadbe.notification.dto;

import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.notification.entity.NotificationType;
import com.esquad.esquadbe.user.dto.UserDTO;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private String name;
    private NotificationType notiType;
    private String message;
    private String createdAt;

    public static NotificationResponseDTO createResponse(Notification notification) {
        return NotificationResponseDTO.builder()
                .name(notification.getReceiver().getUsername())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}
