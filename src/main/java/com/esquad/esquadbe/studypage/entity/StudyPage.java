package com.esquad.esquadbe.studypage.entity;


import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "STUDY_PAGE")
public class StudyPage extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_SPACE_ID", nullable = false)
    private TeamSpace teamSpace;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    // @ManyToOne
    // @JoinColumn(nullable = false)
    // @Column(name = "OWNER")
    // private User owner;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "DESCRIPTION",columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "studyPage")
    private List<StudyPageUser> studyPageUsers = new ArrayList<>();
}