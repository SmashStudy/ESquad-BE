package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserRequestDTO(
    Long id,
    String username,
    String nickname,
    String email,
    String phoneNumber,
    LocalDate birthDay,
    String address
) {

    public User to() {
        return User.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .email(email)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .address(address)
                .build();
    }

    public static UserRequestDTO from(User user) {
        return UserRequestDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
