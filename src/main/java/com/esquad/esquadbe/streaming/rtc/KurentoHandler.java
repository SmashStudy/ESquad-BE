package com.esquad.esquadbe.streaming.rtc;

import com.esquad.esquadbe.streaming.service.KurentoManager;
import com.esquad.esquadbe.streaming.service.KurentoUserRegistry;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class KurentoHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(KurentoHandler.class);
    private final KurentoUserRegistry registry;
    private final KurentoManager roomManager;
    private final Gson gson = new Gson();
}
