package com.esquad.esquadbe.qnaboard.repository;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<BookQnaReply, Long> {

    // 특정 게시글에 달린 댓글 조회
    List<BookQnaReply> findByBoard(BookQnaBoard board);




}
