package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.studypage.entity.StudyPage;
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

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    public StudyPage from(StudyPage studyPage) {
            return StudyPage.builder()
                .id(studyPage.getId())
                .teamSpace(studyPage.getTeamSpace())
                .book(studyPage.getBook())
                .studyPageName(title)
                .startDate(startDate)
                .endDate(endDate)
                .description(description)
                .build();
    }
}
