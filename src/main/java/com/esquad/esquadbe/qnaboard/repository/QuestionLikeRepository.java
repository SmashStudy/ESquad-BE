package com.esquad.esquadbe.qnaboard.repository;

import com.esquad.esquadbe.qnaboard.entity.BookQnaLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionLikeRepository extends JpaRepository<BookQnaLike, Long> {

    // 특정 사용자가 특정 게시물에 좋아요를 눌렀는지 확인
    Optional<BookQnaLike> findByUserIdAndBoardId(Long userId, Long boardId);

    // 특정 게시물의 좋아요 수 반환
    Long countByBoardId(Long boardId);
}
