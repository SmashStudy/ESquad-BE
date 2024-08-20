package com.esquad.esquadbe.notification.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Description;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@EqualsAndHashCode
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

    @ManyToOne
    @JoinColumn(name = "DESTINATION_USER_ID")
    private User user;

    @Column(name = "NOTI_TYPE", length = 20)
    private String notiType;

    @Column(name = "READ_FLAG", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean readFlag = false;

    @Column(nullable = false, length = 50)
    private String message;

}
