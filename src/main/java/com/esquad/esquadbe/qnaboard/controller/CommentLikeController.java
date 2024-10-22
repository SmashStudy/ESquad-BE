package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.service.CommentLikeService;
import com.esquad.esquadbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments/like")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 댓글에 대한 좋아요 토글 (좋아요 추가/취소)
    @PostMapping("/{commentId}")
    public ResponseEntity<String> toggleCommentLike(@AuthenticationPrincipal User user, @PathVariable Long commentId) {
        boolean isLiked = commentLikeService.commentLike(user, commentId);

        if (isLiked) {
            return ResponseEntity.ok("댓글 좋아요가 추가되었습니다.");
        } else {
            return ResponseEntity.ok("댓글 좋아요가 취소되었습니다.");
        }
    }

    // 특정 댓글에 대한 좋아요 개수 조회
    @GetMapping("/count/{commentId}")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        Long likeCount = commentLikeService.getCommentLikeCount(commentId);
        return ResponseEntity.ok(likeCount);
    }
}
