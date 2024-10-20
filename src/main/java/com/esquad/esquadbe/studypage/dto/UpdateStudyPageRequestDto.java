package com.esquad.esquadbe.studypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateStudyPageRequestDto {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
