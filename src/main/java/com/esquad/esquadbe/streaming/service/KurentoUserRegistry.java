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

}
