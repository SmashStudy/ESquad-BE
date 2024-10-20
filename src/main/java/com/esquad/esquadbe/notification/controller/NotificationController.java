package com.esquad.esquadbe.notification.controller;

import com.esquad.esquadbe.notification.dto.NotificationRequestDTO;
import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.notification.service.NotificationService;
import com.esquad.esquadbe.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Tag(name="Notification", description = "유저들의 알림 관련")
@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 구독(subscribe)",
            description = "로그인한 유저들은 알림 서비스에 구독하게 됩니다. '유저명_시간' 으로 식별자가 생성됩니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@Parameter(hidden = true) User user,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        log.info("user, lastEventId: {}, {}", user, lastEventId);
        return ResponseEntity.ok(notificationService.subscribe(user.getUsername(), lastEventId));
    }
}
