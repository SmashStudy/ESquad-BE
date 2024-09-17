package com.esquad.esquadbe.streaming.controller;

import com.esquad.esquadbe.streaming.service.KurentoManager;
import com.esquad.esquadbe.streaming.dto.KurentoRoomDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private static final Logger log = LoggerFactory.getLogger(StreamingController.class);
    private final KurentoManager kurentoManager;

    @PostMapping("/{studyPageId}/start")
    public ResponseEntity<Void> startStreaming(@PathVariable String studyPageId) {
        log.info("스터디 페이지 ID: {}에 대한 스트리밍이 시작되었습니다.", studyPageId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{studyPageId}/stop")
    public ResponseEntity<Void> stopStreaming(@PathVariable String studyPageId) {
        KurentoRoomDto room = kurentoManager.getRoom(studyPageId);
        if (room != null) {
            kurentoManager.removeRoom(room);
            log.info("스터디 페이지 ID: {}에 대한 스트리밍이 중지되었습니다.", studyPageId);
            return ResponseEntity.ok().build();
        } else {
            log.warn("스터디 페이지 ID: {}에 대한 활성 스트리밍을 찾을 수 없습니다.", studyPageId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{studyPageId}")
    public ResponseEntity<String> streamingPage(@PathVariable String studyPageId) {
        log.info("스터디 페이지 ID: {}에 대한 스트리밍 페이지 조회.", studyPageId);
        return ResponseEntity.ok("스터디 페이지 ID: " + studyPageId + "에 대한 스트리밍 페이지입니다.");
    }
}
