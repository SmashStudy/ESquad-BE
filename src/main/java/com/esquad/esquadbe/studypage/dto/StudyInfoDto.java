package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyRemind;
import com.esquad.esquadbe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyInfoDto {
    private String studyPageName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
