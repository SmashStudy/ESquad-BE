package com.esquad.esquadbe.notification.service;

import com.esquad.esquadbe.notification.dto.NotificationResponseDTO;
import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.notification.entity.NotificationType;
import com.esquad.esquadbe.notification.emitter.EmitterRepository;
import com.esquad.esquadbe.notification.repository.NotificationRepository;
import com.esquad.esquadbe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String username, String lastEventId) {
        String emitterId = makeTimeIncludeId(username);

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info("emitter : {}", emitter.toString());

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(username);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [username=" + username + "]");

        if(hasLostData(lastEventId)) {
            sendLostData(lastEventId, username, emitterId, emitter);
        }

        return emitter;
    }

    public void send(User receiver, String message, NotificationType notiType) {
        Notification notification = notificationRepository.save(createNotification(receiver, message, notiType));

        String receiverName = notification.getReceiver().getUsername();
        String eventId = receiverName + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersStartWithByUserName(receiverName);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponseDTO.createResponse(notification));
                }
        );
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        log.info("{} 에게 알림 전송 : [{}]", emitterId, data);

        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            throw new RuntimeException("SSE 연결 오류 발생");
        } finally {
            emitterRepository.deleteById(emitterId);
        }
    }

    private void sendLostData(String lastEventId, String username, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserName(String.valueOf(username));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private String makeTimeIncludeId(String username) {
        return username + "_" + System.currentTimeMillis();
    }

    private Notification createNotification(User receiver, String message, NotificationType notificationType) {
        return Notification.builder()
                .receiver(receiver)
                .notiType(notificationType)
                .message(message)
                .readFlag(false)
                .build();
    }

}
