package com.esquad.esquadbe.qnaboard.dto;

import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;

@Builder
public record CommentRequestDTO(
        Long id,
        String content,
        Long boardId,
        String username,
        boolean replyFlag
) {
    public BookQnaReply to(User writer, BookQnaBoard board) {
        return BookQnaReply.builder()
                .content(content)
                .writer(writer)
                .board(board)
                .replyFlag(replyFlag)
                .likes(0)
                .depth(0)
                .orderNo(0)
                .deletedFlag(false)
                .build();
    }
}
