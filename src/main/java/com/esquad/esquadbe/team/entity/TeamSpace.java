package com.esquad.esquadbe.team.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
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

    @OneToOne(optional = false)
    @JoinColumn(name = "MANAGER_ID", nullable = false)
    private User manager;

    @OneToMany(optional = false, mappedBy = "teamSpace")
    private List<User> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "teamSpace")
    private List<StudyPage> pageList = new ArrayList<>();

    private String description;

    public void setManager(User user) {
        this.manager = user;
        user.setTeamSpace(this);
    }

    public void setMemberList(User user) {
        this.memberList.add(user);
        user.setTeamSpace(this);
    }
}
