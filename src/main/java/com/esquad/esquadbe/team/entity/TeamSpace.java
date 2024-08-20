package com.esquad.esquadbe.team.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "TEAM_SPACE")
public class TeamSpace extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEAM_NAME", unique = true, length = 100, nullable = false)
    private String teamName;

    // @OneToOne(optional = false)
    // @JoinColumn(name = "MANAGER_ID", nullable = false)
    // private User manager;

    @OneToMany(mappedBy = "teamSpace")
    private List<TeamSpaceUser> members = new ArrayList<>();

    @OneToMany(mappedBy = "teamSpace")
    private List<StudyPage> studyPages = new ArrayList<>();

    private String description;

}
