package com.esquad.esquadbe.qnaboard.dto;


import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
public class CommentDTO {
    private Long id;
    private Long boardId;
    private Long writerId;
    private String writerName;
    private String content;
    private Integer likes;
    private String createdAt;
    private String modifiedAt;
    private boolean replyFlag;  // 대댓글 여부

    // 엔티티에서 DTO로 변환하는 메서드
    public static CommentDTO from(BookQnaReply reply) {
        return CommentDTO.builder()
                .id(reply.getId())
                .boardId(reply.getBoard().getId())
                .writerId(reply.getWriter().getId())
                .writerName(reply.getWriter().getNickname())
                .content(reply.getContent())
                .likes(reply.getLikes())
                .createdAt(reply.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(reply.getModifiedAt() != null
                        ? reply.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : null)
                .replyFlag(reply.isReplyFlag())
                .build();
    }
}
