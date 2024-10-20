package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class UserUpdateDTO {
    private String email;
    private String address;
    private String phoneNumber;
    private String nickname;

    public User toEntity(UserUpdateDTO userUpdateDTO, User user) {
        return User.builder()
                .id(user.getId())  // 기존 유저의 ID를 그대로 사용
                .email(this.email != null ? this.email : user.getEmail())  // 이메일 업데이트
                .address(this.address != null ? this.address : user.getAddress())  // 주소 업데이트
                .phoneNumber(user.getPhoneNumber())  // 전화번호 유지
                .nickname(this.nickname != null ? this.nickname : user.getNickname())  // 닉네임 업데이트
                .birthDay(user.getBirthDay())  // 생일은 변경하지 않음
                .password(user.getPassword())  // 비밀번호는 변경하지 않음
                .username(user.getUsername())
                .build();
    }
}
