package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.CommentRequestDTO;
import com.esquad.esquadbe.qnaboard.dto.CommentResponseDTO;
import com.esquad.esquadbe.qnaboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/questions/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> createComment(@PathVariable Long boardId,
                                                @RequestBody CommentRequestDTO requestDTO,
                                                Principal principal) {

        CommentResponseDTO createdComment = commentService.createComment(boardId, requestDTO, principal.getName());

        String responseMessage = String.format("게시글 %d에 댓글 %d이(가) 생성되었습니다.", boardId, createdComment.getId());
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByBoard(@PathVariable Long boardId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long boardId,
                                                @PathVariable Long commentId,
                                                @RequestBody CommentRequestDTO requestDTO,
                                                Principal principal) {

        CommentResponseDTO updatedComment = commentService.updateComment(commentId, requestDTO, principal.getName());

        String responseMessage = String.format("게시글 %d의 댓글 %d이(가) 수정되었습니다.", boardId, updatedComment.getId());
        return ResponseEntity.ok(responseMessage);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long boardId,
                                                @PathVariable Long commentId,
                                                Principal principal) {
        commentService.deleteComment(commentId, principal.getName());

        String responseMessage = String.format("게시글 %d의 댓글 %d이(가) 삭제되었습니다.", boardId, commentId);
        return ResponseEntity.ok(responseMessage);
    }
}
