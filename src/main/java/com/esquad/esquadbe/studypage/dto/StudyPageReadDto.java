package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyPageReadDto {
        private Long id;
        private String image;
        private String title;
}
