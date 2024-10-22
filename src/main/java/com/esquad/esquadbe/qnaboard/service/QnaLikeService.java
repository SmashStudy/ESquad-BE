package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.entity.BookQnaLike;
import com.esquad.esquadbe.qnaboard.repository.LikeRepository;
import com.esquad.esquadbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaLikeService {

    private final LikeRepository likeRepository;
    @Transactional
    public boolean toggleLike(User user, Long boardId) {
        Optional<BookQnaLike> existingLike = likeRepository.findByUserIdAndBoardId(user.getId(), boardId);

        // 이미 좋아요가 눌린 상태라면 좋아요를 취소
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        }

        // 좋아요 추가
        BookQnaLike like = BookQnaLike.builder()
                .user(user)
                .id(boardId)
                .build();
        likeRepository.save(like);
        return true; // 좋아요 추가
    }

    public Long getLikeCount(Long boardId) {
        return likeRepository.countByBoardId(boardId);
    }
}
