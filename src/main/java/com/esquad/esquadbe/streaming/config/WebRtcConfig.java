package com.esquad.esquadbe.streaming.config;

import com.esquad.esquadbe.streaming.service.KurentoManager;
import com.esquad.esquadbe.streaming.service.KurentoUserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebRtcConfig {

    @Value("${kms.url}")
    private String kmsUrl;

    private final KurentoUserRegistry registry;
    private final KurentoManager roomManager;

    @Autowired
    public WebRtcConfig(KurentoUserRegistry registry, KurentoManager roomManager) {
        this.registry = registry;
        this.roomManager = roomManager;
    }
}

