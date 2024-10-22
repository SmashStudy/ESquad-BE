package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.service.QnaLikeService;
import com.esquad.esquadbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/questions/like")
public class QnaLikeController {

    private final QnaLikeService qnaLikeService;

    // 좋아요 토글 (추가/취소)
    @PostMapping("/{boardId}")
    public ResponseEntity<String> toggleLike(@AuthenticationPrincipal User user, @PathVariable Long boardId) {
        boolean isLiked = qnaLikeService.toggleLike(user, boardId);

        // 좋아요 상태에 따라 다른 메시지를 반환
        if (isLiked) {
            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        } else {
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        }
    }

    // 특정 게시글의 좋아요 개수 조회
    @GetMapping("/count/{boardId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long boardId) {
        Long likeCount = qnaLikeService.getLikeCount(boardId);
        return ResponseEntity.ok(likeCount);
    }
}
