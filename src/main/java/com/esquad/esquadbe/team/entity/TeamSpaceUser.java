package com.esquad.esquadbe.team.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TEAM_SPACE_USERS")
@Getter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TeamSpaceUser extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TEAM_SPACE_ID")
    private TeamSpace teamSpace;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User member;

    @Column(length = 20, nullable = false)
    private String role;

    public void joinTeamSpace(TeamSpace teamSpace) {
        this.teamSpace = teamSpace;
    }
}
