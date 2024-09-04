package com.esquad.esquadbe.team.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TEAM_SPACE_USERS")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TeamSpaceUser extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_SPACE_ID")
    private TeamSpace teamSpace;

    @ManyToOne
    @JoinColumn(name = "USERNAME")
    private User member;

    @Column(length = 20, nullable = false)
    private String role;

    public void setTeamSpace(TeamSpace teamSpace) {
        this.teamSpace = teamSpace;
        teamSpace.getMembers().add(this);
    }
}
