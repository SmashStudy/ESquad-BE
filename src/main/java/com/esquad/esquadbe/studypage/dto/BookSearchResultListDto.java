package com.esquad.esquadbe.studypage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookSearchResultListDto {
    @NotNull
    private String lastBuildDate;
    @NotNull
    private int total;
    @NotNull
    private int start;
    @NotNull
    private int display;
    @NotNull
    private List<BookSearchResultItemDto> items;
}