package com.esquad.esquadbe.repository;


import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<BookQnaBoard, Long> {
}
