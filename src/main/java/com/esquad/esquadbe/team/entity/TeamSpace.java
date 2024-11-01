package com.esquad.esquadbe.team.entity;

import java.util.List;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.studypage.entity.StudyPage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
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

    @ToString.Exclude
    @OneToMany(mappedBy = "teamSpace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<TeamSpaceUser> members;

    @OneToMany(mappedBy = "teamSpace")
    private List<StudyPage> studyPages;

    @OneToMany(mappedBy = "teamSpace")
    private List<BookQnaBoard> qnaBoards;

    private String description;

    public void joinMembers() {
        for (TeamSpaceUser member : members) {
            member.joinTeamSpace(this);
        }
    }

}
