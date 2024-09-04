package com.esquad.esquadbe.user.entity;

import java.time.LocalDate;
import java.util.*;

import com.esquad.esquadbe.notification.entity.Notification;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.storage.entity.StoredFile;
import com.esquad.esquadbe.streaming.entity.StreamingParticipant;
import com.esquad.esquadbe.streaming.entity.StreamingSession;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.dto.ResponseDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @Column(name = "USERNAME", nullable = false, unique = true, length = 20)
    private String username;

    @Column(name = "NICKNAME", nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;

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

    @OneToMany(mappedBy = "user")
    private List<StreamingSession> streamingSessions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<StreamingParticipant> streamingParticipants = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserSetting userSetting;

    @OneToMany(mappedBy = "member")
    private List<TeamSpaceUser> teamSpaceUsers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private Set<StudyPageUser> studyPageUsers = new HashSet<>();

    public Optional<ResponseDTO> toResponseDTO() {
        return Optional.ofNullable(ResponseDTO.builder()
                .nickname(this.nickname)
                .build());
    }

}
