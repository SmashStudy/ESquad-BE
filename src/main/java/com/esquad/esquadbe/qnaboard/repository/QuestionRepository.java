package com.esquad.esquadbe.qnaboard.repository;


import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<BookQnaBoard, Long> {
}
