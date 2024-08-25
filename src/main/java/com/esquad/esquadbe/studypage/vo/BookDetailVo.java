package com.esquad.esquadbe.studypage.vo;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookDetailVo {
    private String title;
    private String link;
    private String image;
    private String author;
    private String publisher;
    private String isbn;
    private String description;
    private String pubdate;
}