package com.esquad.esquadbe.qnaboard.dto;


import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QnaBoardRequestsDto {
    private Long id;
    private User writer;
    private String title;
    private StudyPage studyPage;
    private Book book;
    private String content;
    private Integer likes;
}