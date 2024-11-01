package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StudyPageReadDto {
        @NotBlank
        private Long id;
        private String image;
        @NotBlank
        private String title;

        public StudyPageReadDto from(StudyPage studyPage) {
                return StudyPageReadDto.builder()
                        .id(studyPage.getId())
                        .image(studyPage.getBook().getImgPath())
                        .title(studyPage.getStudyPageName())
                        .build();
        }
}
