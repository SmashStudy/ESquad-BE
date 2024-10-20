package com.esquad.esquadbe.studypage.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateStudyPageRequestDto {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
