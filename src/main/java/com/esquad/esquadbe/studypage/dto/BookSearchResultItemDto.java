package com.esquad.esquadbe.studypage.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookSearchResultItemDto {
    @NotNull
    private String title;
    @NotNull
    private String link;
    @NotNull
    private String image;
    @NotNull
    private String author;
    @NotNull
    private String discount;
    @NotNull
    private String publisher;
    @NotNull
    private String isbn;
    @NotNull
    private String description;
    @NotNull
    private String pubdate;
};