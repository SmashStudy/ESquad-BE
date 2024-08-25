package com.esquad.esquadbe.studypage.vo;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BookResultDetailVo {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<BookDetailVo> items;
}