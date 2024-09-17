package com.esquad.esquadbe.streaming.rtc;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class KurentoUserSession {
    private static final Logger log = LoggerFactory.getLogger(KurentoUserSession.class);

    private final String userId;
    private final String nickname;
    private final WebSocketSession session;
    private final String roomId;
    private final Map<String, WebRtcEndpoint> webRtcEndpoints = new ConcurrentHashMap<>();
    private final Map<String, Boolean> sdpNegotiatedMap = new ConcurrentHashMap<>();
    private final List<IceCandidate> pendingIceCandidates = new ArrayList<>();
    private MediaPipeline pipeline;

    private final Queue<JsonObject> messageQueue = new ConcurrentLinkedQueue<>();

    public KurentoUserSession(String userId, String nickname, WebSocketSession session, String roomId) {
        this.userId = userId;
        this.nickname = nickname;
        this.session = session;
        this.roomId = roomId;
    }
}
