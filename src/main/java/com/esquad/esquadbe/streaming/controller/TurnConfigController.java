package com.esquad.esquadbe.streaming.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TurnConfigController {

    @Value("${turn.server.urls}")
    private String turnServerUrl;

    @Value("${turn.server.username}")
    private String turnServerUserName;

    @Value("${turn.server.credential}")
    private String turnServerCredential;

    @GetMapping("/turnCredentials")
    public Map<String, String> turnServerConfig() {
        Map<String, String> turnServerConfig = new HashMap<>();
        turnServerConfig.put("url", turnServerUrl);
        turnServerConfig.put("username", turnServerUserName);
        turnServerConfig.put("credential", turnServerCredential);
        return turnServerConfig;
    }
}
