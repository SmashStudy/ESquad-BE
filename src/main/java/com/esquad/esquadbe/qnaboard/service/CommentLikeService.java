package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReplyLike;
import com.esquad.esquadbe.qnaboard.repository.CommentLikeRepository;
import com.esquad.esquadbe.qnaboard.repository.CommentRepository;
import com.esquad.esquadbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public boolean commentLike(User user, Long commentId) {
        // 해당 사용자가 이미 댓글에 좋아요를 눌렀는지 확인
        Optional<BookQnaReplyLike> existingLike = commentLikeRepository.findByUserIdAndReplyId(user.getId(), commentId);

        // 이미 좋아요가 눌린 상태라면 좋아요 취소
        if (existingLike.isPresent()) {
            commentLikeRepository.delete(existingLike.get());
            return false; // 좋아요 취소됨
        }

        // 댓글을 ID로 조회 (존재하지 않으면 예외 발생)
        BookQnaReply reply = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        // 좋아요 추가
        BookQnaReplyLike like = BookQnaReplyLike.builder()
                .user(user)
                .reply(reply)
                .build();

        commentLikeRepository.save(like);
        return true; // 좋아요 추가
    }

    // 댓글 좋아요 수 반환
    public Long getCommentLikeCount(Long commentId) {
        return commentLikeRepository.countByReplyId(commentId);
    }
}
