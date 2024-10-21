package com.esquad.esquadbe.qnaboard.repository;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaLike;
import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookQnaLikeRepository extends JpaRepository<BookQnaLike, Long> {
    // 특정 사용자와 게시글에 대한 좋아요 여부 확인
    BookQnaLike findByUserAndBoard(User user, BookQnaBoard board);
}