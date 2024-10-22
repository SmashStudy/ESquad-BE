package com.esquad.esquadbe.studypage.dto;

import jakarta.validation.constraints.NotBlank;
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
public class StudyPageCreateDto {
    @NotBlank
    private BookSearchResultItemDto bookDto;
    @NotBlank
    private StudyInfoDto studyInfoDto;
    @NotNull
    private List<StudyRemindDto> reminds;
    @NotBlank
    private List<Long> userIds;
}

