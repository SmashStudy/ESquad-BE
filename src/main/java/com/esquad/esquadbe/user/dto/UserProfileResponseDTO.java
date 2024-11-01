package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserProfileResponseDTO(
    Long id,
    String username,
    String nickname
) {

    public User to() {
        return User.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .build();
    }

    public static UserProfileResponseDTO from(User user) {
        return UserProfileResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
