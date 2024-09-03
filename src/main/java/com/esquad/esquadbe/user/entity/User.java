package com.esquad.esquadbe.user.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.storage.entity.StoredFile;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;

import com.esquad.esquadbe.streaming.entity.StreamingParticipant;
import com.esquad.esquadbe.streaming.entity.StreamingSession;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "USERS")
public class User extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @Pattern(regexp = "^[a-zA-Z]{8,12}$", message = "아이디는 영어만 포함한 8~12자여야 합니다.")
    private String username;

    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
    private String nickname;

    @Column(nullable = false, length = 60)
//    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다.")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=.*[!~&+@]).*$", message = "비밀번호는 대소문자와 특수문자(!~&+@)를 포함해야 합니다.")
    private String password;

    @Column(nullable = false, length = 25)
    private String email;

    @Column(nullable = false, length = 12)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column(nullable = false, length = 255)
    private String address;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Notification> notifications = new ArrayList<>();

     @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
     private List<StoredFile> storedFiles = new ArrayList<>();

    @OneToMany(mappedBy = "writer", fetch = FetchType.EAGER)
    private List<BookQnaBoard> bookQnaBoards = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<StreamingSession> streamingSessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<StreamingParticipant> streamingParticipants = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserSetting userSetting;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<TeamSpaceUser> teamSpaceUsers = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private Set<StudyPageUser> studyPageUsers = new HashSet<>();

}
