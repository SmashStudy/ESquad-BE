package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.entity.BookQnaLike;
import com.esquad.esquadbe.qnaboard.repository.QuestionLikeRepository;
import com.esquad.esquadbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionLikeService {

    private final QuestionLikeRepository questionLikeRepository;
    @Transactional
    public boolean toggleLike(User user, Long boardId) {
        Optional<BookQnaLike> existingLike = questionLikeRepository.findByUserIdAndBoardId(user.getId(), boardId);

        // 이미 좋아요가 눌린 상태라면 좋아요를 취소
        if (existingLike.isPresent()) {
            questionLikeRepository.delete(existingLike.get());
            return false;
        }

        // 좋아요 추가
        BookQnaLike like = BookQnaLike.builder()
                .user(user)
                .id(boardId)
                .build();
        questionLikeRepository.save(like);
        return true; // 좋아요 추가
    }

    public Long getLikeCount(Long boardId) {
        return questionLikeRepository.countByBoardId(boardId);
    }
}
