package com.esquad.esquadbe.notification.dto;

import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.notification.entity.NotificationType;
import lombok.Builder;

@Builder
public record NotificationResponseDTO (
        Long id,
        String name,
        NotificationType notiType,
        String message,
        String createdAt
) {
    public static NotificationResponseDTO from(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .name(notification.getReceiver().getUsername())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}
