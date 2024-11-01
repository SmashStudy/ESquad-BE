package com.esquad.esquadbe.user.dto;

import com.esquad.esquadbe.user.entity.User;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserGetResponseDTO(long id, String username, String password, String nickname, String phoneNumber, String email, String address, LocalDate birthDay) {

    public static UserGetResponseDTO of(User user) {
        return UserGetResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .address(user.getAddress())
                .birthDay(user.getBirthDay())
                .build();
    }

}
