package com.esquad.esquadbe.qnaboard.dto;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
public class QnaBoardResponseDTO {
    private Long id;
    private String title;
    private String writerName;
    private String book;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Integer likes;

    // 정적 팩토리 메서드
    public static QnaBoardResponseDTO from(BookQnaBoard bookQnaBoard) {
        return QnaBoardResponseDTO.builder()
                .id(bookQnaBoard.getId())
                .title(bookQnaBoard.getTitle())
                .writerName(bookQnaBoard.getWriter())
                .book(bookQnaBoard.getBook())
                .content(bookQnaBoard.getContent())
                .createdAt(bookQnaBoard.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(bookQnaBoard.getModifiedAt() != null
                        ? bookQnaBoard.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : null)  // 수정일시 추가
                .likes(bookQnaBoard.getLikes())
                .build();
    }
}
