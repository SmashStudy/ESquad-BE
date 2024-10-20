package com.esquad.esquadbe.notification.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String eventCacheId, Object event);
    Map<String, SseEmitter> findAllEmittersStartWithByUserName(String userId);
    Map<String, Object> findAllEventCacheStartWithByUserName(String userId);
    void deleteById(String id);
    void deleteAllEmitterStartWithUserName(String userId);
    void deleteAllEventCacheStartWithUserName(String userId);
}
