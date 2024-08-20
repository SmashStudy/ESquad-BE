package com.esquad.esquadbe.studypage.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.notification.entity.AlertDayType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Description;

import java.time.LocalTime;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "STUDY_REMIND")
@Description("스터디 리마인드")
public class StudyRemind extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STUDY_PAGE_ID")
    private StudyPage studyPage;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "DAY_AT", nullable = false)
    private AlertDayType dayType;

    @Column(name = "TIME_AT")
    private LocalTime timeAt;

    @Column(length = 100)
    private String description;

    @Column(name = "REMIND_FLAG", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean remindFlag = true;

}
