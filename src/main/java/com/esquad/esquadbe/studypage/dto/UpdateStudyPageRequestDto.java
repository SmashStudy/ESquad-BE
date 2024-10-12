package com.esquad.esquadbe.studypage.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateStudyPageRequestDto {
    private Long userId; // 수정 요청한 유저의 ID
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
