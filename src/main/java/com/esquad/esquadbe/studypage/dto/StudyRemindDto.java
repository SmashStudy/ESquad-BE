package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.notification.entity.AlertDayType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyRemindDto {
    @NotBlank
    private AlertDayType dayType;
    @NotBlank
    private LocalTime timeAt;
    private String description;
}
