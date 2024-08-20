package com.esquad.esquadbe.studypage.entity;


import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "STUDY_PAGE")
public class StudyPage extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teamSpaceId", nullable = false)
    @Column(name = "TEAM_SPACE_ID")
    private TeamSpace teamSpace;

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false)
    @Column(name = "BOOK_ID")
    private Book book;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Column(name = "OWNER")
    private User owner;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "DESCRIPTION",columnDefinition = "TEXT")
    private String description;
}