package com.esquad.esquadbe.streaming.entity;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "STREAMING_SESSIONS")
public class StreamingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "CHANNEL_ID", nullable = false)
    private StudyPage channel;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "STREAM_TYPE", length = 10, nullable = false)
    private String streamType;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StreamingParticipant> participants;
}
