package com.esquad.esquadbe.streaming.entity;

import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "STREAMING_PARTICIPANTS")
public class StreamingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SESSION_ID", nullable = false)
    private StreamingSession session;

   @ManyToOne
   @JoinColumn(name = "USER_ID", nullable = false)
   private User user;

    @Column(name = "VOICE_PARTICIPATION_FLAG", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean voiceParticipationFlag = false;
}
