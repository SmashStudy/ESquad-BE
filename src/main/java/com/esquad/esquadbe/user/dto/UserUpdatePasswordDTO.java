package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class UserUpdatePasswordDTO {
    private String currentPassword;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!~&+@])[a-z\\d!~&+@]{6,16}$",
            message = "비밀번호는 소문자, 숫자, 특수문자(!~&+@)를 포함한 6~16자여야 합니다."
    )
    private String newPassword;

    public User toEntity(String encodedPassword, User user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .nickname(user.getNickname())
                .birthDay(user.getBirthDay())
                .password(encodedPassword)
                .username(user.getUsername())
                .build();
    }
}
