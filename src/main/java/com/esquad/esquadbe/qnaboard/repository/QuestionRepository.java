package com.esquad.esquadbe.qnaboard.repository;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<BookQnaBoard, Long> {

    Page<BookQnaBoard> findAll(Pageable pageable);

    // 작성자(writer)로 게시글 검색
    Page<BookQnaBoard> findByWriter(User writer, Pageable pageable);

    // 제목으로 게시글 검색
    Page<BookQnaBoard> findByTitleContaining(String title, Pageable pageable);
}
