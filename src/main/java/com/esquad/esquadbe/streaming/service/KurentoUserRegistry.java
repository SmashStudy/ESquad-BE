package com.esquad.esquadbe.streaming.service;

import com.esquad.esquadbe.streaming.rtc.KurentoUserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class KurentoUserRegistry {

    private static final Logger log = LoggerFactory.getLogger(KurentoUserRegistry.class);
    private final ConcurrentHashMap<String, KurentoUserSession> usersByName = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, KurentoUserSession> usersBySessionId = new ConcurrentHashMap<>();
}
