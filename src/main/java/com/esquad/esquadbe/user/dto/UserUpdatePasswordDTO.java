package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
                .id(user.getId())  // 기존 유저의 ID를 그대로 사용
                .email(user.getEmail())  // 이메일 업데이트
                .address(user.getAddress())  // 주소 업데이트
                .phoneNumber(user.getPhoneNumber())  // 전화번호 유지
                .nickname(user.getNickname())  // 닉네임 업데이트
                .birthDay(user.getBirthDay())  // 생일은 변경하지 않음
                .password(encodedPassword)  // 비밀번호 변경
                .username(user.getUsername())
                .build();
    }
}
