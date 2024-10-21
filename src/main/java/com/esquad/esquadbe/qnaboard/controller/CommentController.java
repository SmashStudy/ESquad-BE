package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.CommentDTO;
import com.esquad.esquadbe.qnaboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestParam("boardId") Long boardId,
            @RequestParam("writerId") Long writerId,
            @RequestParam("content") String content,
            @RequestParam("replyFlag") boolean replyFlag) {
        CommentDTO createdComment = commentService.createComment(boardId, writerId, content, replyFlag);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestParam("writerId") Long writerId,
            @RequestParam("content") String content) {
        CommentDTO updatedComment = commentService.updateComment(commentId, writerId, content);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam("writerId") Long writerId) {
        commentService.deleteComment(commentId, writerId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByBoardId(@RequestParam("boardId") Long boardId) {
        List<CommentDTO> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }


    // 댓글 좋아요 추가/취소
    @PostMapping("/like/{commentId}")
    public ResponseEntity<String> likeComment(
            @PathVariable Long commentId,
            @RequestParam("userId") Long userId) {
        String result = commentService.likeComment(commentId, userId);
        return ResponseEntity.ok(result);
    }
}
