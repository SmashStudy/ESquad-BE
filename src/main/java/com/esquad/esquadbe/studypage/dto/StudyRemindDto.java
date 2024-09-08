package com.esquad.esquadbe.studypage.dto;

import com.esquad.esquadbe.notification.entity.AlertDayType;
import jakarta.persistence.Column;
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
    private AlertDayType dayType;
    private LocalTime timeAt;
    private String description;
}
