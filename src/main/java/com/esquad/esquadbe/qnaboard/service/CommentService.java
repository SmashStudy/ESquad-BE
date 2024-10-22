package com.esquad.esquadbe.qnaboard.service;


import com.esquad.esquadbe.qnaboard.dto.CommentDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.exception.UnauthorizedException;
import com.esquad.esquadbe.qnaboard.repository.CommentRepository;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    public CommentDTO createComment(Long boardId, Long writerId, String content, boolean replyFlag) {
        BookQnaBoard board = questionRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다: " + boardId));
        User writer = userRepository.findById(writerId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다: " + writerId));

        BookQnaReply reply = BookQnaReply.builder()
                .board(board)
                .writer(writer)
                .content(content)
                .likes(0)
                .replyFlag(replyFlag)
                .depth(0) // 기본 댓글의 depth
                .orderNo(0) // 기본 댓글의 order
                .deletedFlag(false)
                .build();

        BookQnaReply savedReply = commentRepository.save(reply);
        return CommentDTO.from(savedReply);
    }


    // 댓글 수정
    public CommentDTO updateComment(Long commentId, Long writerId, String content) {
        BookQnaReply reply = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        if (!reply.getWriter().getId().equals(writerId)) {
            throw new UnauthorizedException("댓글 수정 권한이 없습니다.");
        }

        reply.setContent(content);
        reply.setModifiedAt(java.time.LocalDateTime.now());

        BookQnaReply updatedReply = commentRepository.save(reply);
        return CommentDTO.from(updatedReply);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, Long writerId) {
        BookQnaReply reply = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        if (!reply.getWriter().getId().equals(writerId)) {
            throw new UnauthorizedException("댓글 삭제 권한이 없습니다.");
        }


        commentRepository.delete(reply);
    }


    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        BookQnaBoard board = questionRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다: " + boardId));

        List<BookQnaReply> replies = commentRepository.findByBoard(board);

        // BookQnaReply 리스트를 CommentDTO 리스트로 변환
        return replies.stream().map(CommentDTO::from).collect(Collectors.toList());
    }
}

