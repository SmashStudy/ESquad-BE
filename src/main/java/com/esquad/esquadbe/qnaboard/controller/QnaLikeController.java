package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.service.QuestionLikeService;
import com.esquad.esquadbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/questions")
public class QnaLikeController {

    private final QuestionLikeService questionLikeService;

    // 좋아요 (추가/취소)
    @PostMapping("/{boardId}/like")
    public ResponseEntity<String> toggleLike(@AuthenticationPrincipal User user, @PathVariable Long boardId) {
        boolean isLiked = questionLikeService.toggleLike(user, boardId);

        // 좋아요 상태에 따라 다른 메시지를 반환
        return isLiked ? ResponseEntity.ok("좋아요가 추가되었습니다.") :
                ResponseEntity.ok("좋아요가 취소되었습니다.");
    }

    // 특정 게시글의 좋아요 개수 조회
    @GetMapping("/{boardId}/likes")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long boardId) {
        Long likeCount = questionLikeService.getLikeCount(boardId);
        return ResponseEntity.ok(likeCount);
    }
}
