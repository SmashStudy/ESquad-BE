package com.esquad.esquadbe.notification.emitter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        log.info(emitters.toString());
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmittersStartWithByUserName(String username) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(username))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByUserName(String username) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(username))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithUserName(String username) {
        emitters.forEach((key, value) -> {
            if (key.startsWith(username)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public void deleteAllEventCacheStartWithUserName(String username) {
        eventCache.forEach((key, value) -> {
            if (key.startsWith(username)) {
                eventCache.remove(key);
            }
        });
    }


}
