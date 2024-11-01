package com.esquad.esquadbe.notification.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Description;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Description("알림 정보를 저장하는 테이블")
public class Notification extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "DESTINATION_USER_ID")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "NOTI_TYPE", length = 10, nullable = false)
    private NotificationType notiType;

    @Column(name = "READ_FLAG", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean readFlag = false;

    @Column(nullable = false, length = 50)
    private String message;

    public void read() {
        readFlag = true;
    }
}
