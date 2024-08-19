package com.esquad.esquadbe.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_SETTINGS")
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "USER_ID", nullable = false)
//    private User user;

    @Column(name = "VOICE_FLAG", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean voiceFlag = false;
}

