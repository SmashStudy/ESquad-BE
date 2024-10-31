package com.esquad.esquadbe.qnaboard.dto;

import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentResponseDTO {
    private Long id;
    private Long boardId;
    private Long writerId;
    private String writerName;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean replyFlag;
    private boolean deletedFlag;

    public static CommentResponseDTO from(BookQnaReply reply) {
        return CommentResponseDTO.builder()
                .id(reply.getId())
                .boardId(reply.getBoard().getId())
                .writerId(reply.getWriter().getId())
                .writerName(reply.getWriter().getNickname())
                .content(reply.getContent())
                .likes(reply.getLikes())
                .createdAt(reply.getCreatedAt())
                .modifiedAt(reply.getModifiedAt())
                .replyFlag(reply.isReplyFlag())
                .deletedFlag(reply.isDeletedFlag())
                .build();
    }
}
