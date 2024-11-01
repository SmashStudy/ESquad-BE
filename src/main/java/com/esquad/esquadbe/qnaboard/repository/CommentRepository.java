package com.esquad.esquadbe.qnaboard.repository;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<BookQnaReply, Long> {
    List<BookQnaReply> findByBoard(BookQnaBoard board);
}
