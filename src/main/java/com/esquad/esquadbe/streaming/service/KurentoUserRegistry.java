package com.esquad.esquadbe.streaming.service;

import com.esquad.esquadbe.streaming.rtc.KurentoUserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class KurentoUserRegistry {

    private static final Logger log = LoggerFactory.getLogger(KurentoUserRegistry.class);
    private final ConcurrentHashMap<String, KurentoUserSession> usersByName = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, KurentoUserSession> usersBySessionId = new ConcurrentHashMap<>();

    public KurentoUserSession getBySession(WebSocketSession session) {
        KurentoUserSession user = usersBySessionId.get(session.getId());
        log.info("세션 ID로 사용자 조회: {}", session.getId());
        log.debug("조회된 사용자: {}", user != null ? user.getUserId() : "없음");
        return user;
    }

    public synchronized void register(KurentoUserSession user) {
        removeBySession(user.getSession());

        usersByName.put(user.getUserId(), user);
        usersBySessionId.put(user.getSession().getId(), user);
        log.info("사용자가 등록되었습니다: 사용자 ID: {}, 세션 ID: {}", user.getUserId(), user.getSession().getId());

        logAllParticipants();
    }

    public synchronized KurentoUserSession removeBySession(WebSocketSession session) {
        KurentoUserSession user = usersBySessionId.remove(session.getId());
        if (user != null) {
            usersByName.remove(user.getUserId());
            user.close(); // 사용자 세션과 관련된 리소스 해제
            log.info("사용자가 제거되었습니다: 사용자 ID: {}, 세션 ID: {}", user.getUserId(), session.getId());
        } else {
            log.warn("해당 세션 ID에 해당하는 사용자를 찾을 수 없습니다: {}", session.getId());
        }

        // 현재 사용자 및 세션 목록 로그 출력
        log.debug("현재 등록된 사용자 목록: {}", usersByName.keySet());
        log.debug("현재 등록된 세션 목록: {}", usersBySessionId.keySet());
        return user;
    }

    public void logAllParticipants() {
        log.info("현재 등록된 사용자 목록: {}", usersByName.keySet());
        log.info("현재 등록된 세션 목록: {}", usersBySessionId.keySet());
    }

    public KurentoUserSession getByName(String name) {
        KurentoUserSession user = usersByName.get(name);
        log.info("이름으로 사용자 조회: {}", name);
        log.debug("조회된 사용자: {}", user != null ? user.getUserId() : "없음");
        return user;
    }
}
