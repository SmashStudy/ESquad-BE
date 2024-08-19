package com.esquad.esquadbe.team.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "TEAM_SPACE")
public class TeamSpace extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEAM_NAME", unique = true)
    private String teamName;

    @OneToOne(optional = false)
    @JoinColumn(name = "MANAGER_ID", nullable = false)
    private User manager;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private User member;

    @OneToMany(mappedBy = "teamSpace")
    private List<StudyPage> pageList = new ArrayList<>();

    private String description;

    public void setManager(User user) {
        this.manager = user;
        user.setTeamSpace(this);
    }

    public void setMember(User user) {
        this.member = user;
        user.setTeamSpace(this);
    }
}
