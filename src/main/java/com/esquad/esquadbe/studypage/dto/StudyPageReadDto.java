package com.esquad.esquadbe.studypage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyPageReadDto {
        @NotBlank
        private Long id;
        private String image;
        @NotBlank
        private String title;
}
