package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.exception.CommentNotFoundException;
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

        Optional<BookQnaReplyLike> existingLike = commentLikeRepository.findByUserIdAndReplyId(user.getId(), commentId);


        if (existingLike.isPresent()) {
            commentLikeRepository.delete(existingLike.get());
            return false;
        }


        BookQnaReply reply = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);


        BookQnaReplyLike like = BookQnaReplyLike.builder()
                .user(user)
                .reply(reply)
                .build();

        commentLikeRepository.save(like);
        return true;
    }


    public Long getCommentLikeCount(Long commentId) {
        return commentLikeRepository.countByReplyId(commentId);
    }
}
