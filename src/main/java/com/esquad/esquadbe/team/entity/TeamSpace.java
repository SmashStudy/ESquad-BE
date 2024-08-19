package com.esquad.esquadbe.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TEAM_SPACE")
public class TeamSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEAM_NAME", unique = true)
    private String teamName;

    @OneToOne(optional = false)
    @JoinColumn(name = "MANAGER_ID", nullable = false)
    private Users manager;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Users member;

    @OneToMany(mappedBy = "teamSpace")
    private List<StudyPage> pageList = new ArrayList<>();

    private String description;

    public void setManager(Users user) {
        this.manager = user;
        user.setTeamSpace(this);
    }

    public void setMember(Users user) {
        this.member = user;
        user.setTeamSpace(this);
    }
}
