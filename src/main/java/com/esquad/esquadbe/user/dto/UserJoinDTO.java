package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UserJoinDTO {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{6,12}$",
            message = "아이디는 영어와 숫자로 구성된 6~12자여야 합니다."
    )
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
//    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다.")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=.*[!~&+@]).*$", message = "비밀번호는 대소문자와 특수문자(!~&+@)를 포함해야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!~&+@])[a-z\\d!~&+@]{6,16}$",
            message = "비밀번호는 소문자, 숫자, 특수문자(!~&+@)를 포함한 6~16자여야 합니다."
    )

    private String password;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDay;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    private String detailAddress;

    private String extraAddress;

    private String nickname;


    public void setDefaultNickname() {
        this.nickname = this.username;
    }

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        String fullAddress = String.format("%s %s %s", address, detailAddress, extraAddress).trim();
        return User.builder()
                .username(this.username)
                .password(bCryptPasswordEncoder.encode(this.password))
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .birthDay(this.birthDay)
                .address(fullAddress)
                .nickname(this.nickname)
                .build();
    }

}