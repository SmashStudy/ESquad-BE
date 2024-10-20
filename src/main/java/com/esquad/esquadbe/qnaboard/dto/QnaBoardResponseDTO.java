package com.esquad.esquadbe.qnaboard.dto;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class QnaBoardResponseDTO  {
    private Long id;
    private String title;
    private Long userId;
    private String writerName;
    private Long bookId;
    private String bookTitle;
    private Long teamSpaceId;
    private String teamSpaceName;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Integer likes;

    // 정적 팩토리 메서드
    public static QnaBoardResponseDTO from(BookQnaBoard bookQnaBoard) {
        return QnaBoardResponseDTO.builder()
                .id(bookQnaBoard.getId())
                .title(bookQnaBoard.getTitle())
                .userId(bookQnaBoard.getWriter().getId())
                .writerName(bookQnaBoard.getWriter().getNickname())
                .bookId(bookQnaBoard.getBook().getId())
                .bookTitle(bookQnaBoard.getBook().getTitle())
                .teamSpaceId(bookQnaBoard.getTeamSpace().getId())
                .teamSpaceName(bookQnaBoard.getTeamSpace().getTeamName())
                .content(bookQnaBoard.getContent())
                .createdAt(bookQnaBoard.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(bookQnaBoard.getModifiedAt() != null
                        ? bookQnaBoard.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : null)
                .likes(bookQnaBoard.getLikes())
                .build();
    }



}
