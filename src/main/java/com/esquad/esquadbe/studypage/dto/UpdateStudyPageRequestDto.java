package com.esquad.esquadbe.studypage.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String title;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;

    private String description;
}
