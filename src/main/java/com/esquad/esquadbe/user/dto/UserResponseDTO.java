package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;
import lombok.*;

import java.time.LocalDate;

@Builder
public record UserResponseDTO(
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

    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
