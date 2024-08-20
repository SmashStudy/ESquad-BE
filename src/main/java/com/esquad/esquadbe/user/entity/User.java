package com.esquad.esquadbe.user.entity;

import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.storage.entity.StoredFile;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@SuperBuilder
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
    private List<Notification> notifications = new ArrayList<>();

     @OneToMany(mappedBy = "user")
     private List<StoredFile> storedFiles = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<BookQnaBoard> bookQnaBoards = new ArrayList<>();

    // @OneToMany(mappedBy = "user")
    // private List<StreamingSession> streamingSessions = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserSetting userSetting;

    @OneToMany(mappedBy = "member")
    private List<TeamSpaceUser> teamSpaceUsers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private Set<StudyPageUser> studyPageUsers = new HashSet<>();

}
