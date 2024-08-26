package com.esquad.esquadbe.user.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=.*[!~&+@]).*$", message = "비밀번호는 대소문자와 특수문자(!~&+@)를 포함해야 합니다.")
    private String password;

    @Column(nullable = false, length = 25)
    private String email;

    @Column(name = "PHONE_NO", nullable = false, length = 12)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column(nullable = false, length = 255)
    private String address;

}