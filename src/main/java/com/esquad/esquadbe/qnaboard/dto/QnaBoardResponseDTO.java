package com.esquad.esquadbe.qnaboard.dto;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
public class QnaBoardResponseDTO  {
    private Long id;
    private String title;
    private User user;
    private Book book;
    private TeamSpace teamSpace;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Integer likes;

    // 정적 팩토리 메서드
    public static QnaBoardResponseDTO from(BookQnaBoard bookQnaBoard) {
        return QnaBoardResponseDTO.builder()
                .id(bookQnaBoard.getId())
                .title(bookQnaBoard.getTitle())
                .user(bookQnaBoard.getWriter())
                .book(bookQnaBoard.getBook())
                .teamSpace(bookQnaBoard.getTeamSpace())
                .content(bookQnaBoard.getContent())
                .createdAt(bookQnaBoard.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(bookQnaBoard.getModifiedAt() != null
                        ? bookQnaBoard.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : null)
                .likes(bookQnaBoard.getLikes())
                .build();
    }
}
