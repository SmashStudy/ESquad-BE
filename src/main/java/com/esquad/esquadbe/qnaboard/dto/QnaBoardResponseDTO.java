package com.esquad.esquadbe.qnaboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaBoardResponseDTO {
    private Long id;
    private String writerName;
    private Long studyPageId;
    private Long bookId;
    private String title;
    private String content;
    private Integer likes;
    private String createdAt;
    private String updatedAt;
}
