package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDTO {


    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z]{8,12}$", message = "아이디는 영어만 포함한 8~12자여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=.*[!~&+@]).*$", message = "비밀번호는 대소문자와 특수문자(!~&+@)를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String phoneNo;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDay;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    private String nickname;


    public void setDefaultNickname() {
            this.nickname = this.userId;
    }

    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .userName(this.userName)
                .email(this.email)
                .phoneNo(this.phoneNo)
                .birthDay(this.birthDay)
                .address(this.address)
                .nickname(this.nickname)
                .build();
    }

    public void encodePassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.password = bCryptPasswordEncoder.encode(this.password);
    }
}