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
                .id(user.getId())
                .email(this.email != null ? this.email : user.getEmail())
                .address(this.address != null ? this.address : user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .nickname(this.nickname != null ? this.nickname : user.getNickname())
                .birthDay(user.getBirthDay())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }
}
