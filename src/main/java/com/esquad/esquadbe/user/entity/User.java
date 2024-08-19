package com.esquad.esquadbe.user.entity;

import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false, unique = true, length = 20)
    private String userId;

    @Column(name = "NICKNAME", nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;

    @Column(name = "USER_NAME", nullable = false, length = 12)
    private String userName;

    @Column(name = "EMAIL", nullable = false, length = 25)
    private String email;

    @Column(name = "PHONE_NO", nullable = false, length = 12)
    private String phoneNo;

    @Column(name = "BIRTH_DAY", nullable = false)
    private LocalDate birthDay;

    @Column(name = "ADDRESS", nullable = false, length = 255)
    private String address;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();;

    @OneToMany(mappedBy = "user")
    private List<BookQNAFile> bookQNAFiles = new ArrayList<>();;

    @OneToMany(mappedBy = "user")
    private List<BookQNABoard> bookQNABoards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<StreamingSession> streamingSessions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserSetting userSetting;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private TeamSpace teamSpace;

    @ManyToMany(mappedBy = "user")
    private Set<StudyPage> studyPages = new HashSet<>();

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}