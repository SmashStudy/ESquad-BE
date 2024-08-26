package com.esquad.esquadbe.studypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookSearchDetailResultDto {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<BookSearchDetailDto> items;
}