package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StudyInfoDto {
    @NotNull
    private String studyPageName;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private String description;

    //create
    public StudyPage from(TeamSpace teamSpace, Book book) {
        return StudyPage.builder()
                .teamSpace(teamSpace)
                .book(book)
                .studyPageName(studyPageName)
                .startDate(startDate)
                .endDate(endDate)
                .description(description)
                .build();
    }

    //update
    public static StudyInfoDto to(StudyPage studyPage) {
        return StudyInfoDto.builder()
                .studyPageName(studyPage.getStudyPageName())
                .startDate(studyPage.getStartDate())
                .endDate(studyPage.getEndDate())
                .description(studyPage.getDescription())
                .build();
    }

}
