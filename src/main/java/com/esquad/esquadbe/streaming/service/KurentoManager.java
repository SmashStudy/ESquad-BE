package com.esquad.esquadbe.streaming.service;

import com.esquad.esquadbe.streaming.util.ChannelRoomMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KurentoManager {

    private final Logger log = LoggerFactory.getLogger(KurentoManager.class);
    private final ChannelRoomMap roomMap = ChannelRoomMap.getInstance();
    private static final int MAX_PARTICIPANTS = 12;
}
