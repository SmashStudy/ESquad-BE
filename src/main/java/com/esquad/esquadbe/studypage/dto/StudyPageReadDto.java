package com.esquad.esquadbe.studypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyPageReadDto {
        private Long id;
        private String image;
        private String title;
}
