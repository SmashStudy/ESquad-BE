package com.esquad.esquadbe.notification.dto;

import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.notification.entity.NotificationType;
import com.esquad.esquadbe.user.dto.UserDTO;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    private String receiver;
    private String message;
    private NotificationType notiType;
    private boolean readFlag;
    private String url;
}
