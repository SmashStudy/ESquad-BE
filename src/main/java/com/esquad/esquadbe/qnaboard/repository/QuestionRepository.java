package com.esquad.esquadbe.qnaboard.repository;


import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<BookQnaBoard, Long> {
    Page<BookQnaBoard> findAll(Pageable pageable);
}
