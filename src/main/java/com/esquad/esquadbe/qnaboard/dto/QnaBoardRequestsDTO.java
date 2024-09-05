package com.esquad.esquadbe.qnaboard.dto;


import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QnaBoardRequestsDTO {
    private Long id;
    private User writer;
    private String title;
    private StudyPage studyPage;
    private Book book;
    private String content;
    private Integer likes;

    // 정적 팩토리 메서드 추가
    public static QnaBoardRequestsDTO fromEntity(BookQnaBoard bookQnaBoard) {
        return QnaBoardRequestsDTO.builder()
                .id(bookQnaBoard.getId())
                .writer(bookQnaBoard.getWriter())
                .title(bookQnaBoard.getTitle())
                .studyPage(bookQnaBoard.getStudyPage())
                .book(bookQnaBoard.getBook())
                .content(bookQnaBoard.getContent())
                .likes(bookQnaBoard.getLikes())
                .build();
    }

}