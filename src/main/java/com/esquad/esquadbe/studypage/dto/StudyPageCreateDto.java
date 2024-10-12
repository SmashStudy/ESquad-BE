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
public class StudyPageCreateDto {
    private BookSearchResultItemDto bookDto;
    private StudyInfoDto studyInfoDto;
    private List<StudyRemindDto> reminds;
    private List<Long> userIds;
}

