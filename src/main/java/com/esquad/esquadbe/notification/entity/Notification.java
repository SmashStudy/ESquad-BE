package com.esquad.esquadbe.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;

import java.sql.Date;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Description("알림 정보를 저장하는 테이블")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DESTINATION_USER_ID")
    private Users user;

    @Column(name = "NOTI_TYPE")
    private String notiType;

    @Column(name = "READ_FLAG")
    private Long readFlag;

    @Column(nullable = false)
    private String message;

}
