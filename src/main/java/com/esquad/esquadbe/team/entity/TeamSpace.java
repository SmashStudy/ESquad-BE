package com.esquad.esquadbe.team.entity;

import java.util.ArrayList;
import java.util.List;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.studypage.entity.StudyPage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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

    @OneToMany(mappedBy = "teamSpace")
    private List<TeamSpaceUser> members = new ArrayList<>();

    @OneToMany(mappedBy = "teamSpace")
    private List<StudyPage> studyPages = new ArrayList<>();

    @OneToMany(mappedBy = "teamSpace")
    private List<BookQnaBoard> qnaBoards = new ArrayList<>();

    private String description;

}
