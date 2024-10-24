package com.esquad.esquadbe.qnaboard.dto;


import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


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
    private boolean replyFlag;

    public static CommentDTO from(BookQnaReply reply) {
        return CommentDTO.builder()
                .id(reply.getId())
                .boardId(reply.getBoard().getId())
                .writerId(reply.getWriter().getId())
                .writerName(reply.getWriter().getNickname())
                .content(reply.getContent())
                .likes(reply.getLikes())
                .replyFlag(reply.isReplyFlag())
                .build();
    }
}
