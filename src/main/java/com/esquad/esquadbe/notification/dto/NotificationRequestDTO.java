package com.esquad.esquadbe.notification.dto;

import com.esquad.esquadbe.notification.entity.NotificationType;
import lombok.Builder;


@Builder
public record NotificationRequestDTO (
        Long id,
        String receive,
        String message,
        NotificationType notiType,
        boolean readFlag
) {

}
