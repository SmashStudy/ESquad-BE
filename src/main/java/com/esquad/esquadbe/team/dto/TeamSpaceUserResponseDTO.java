package com.esquad.esquadbe.team.dto;

import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.dto.UserProfileResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record TeamSpaceUserResponseDTO(
                                          @NotNull Long id,
                                          @ToString.Exclude
                                          @NotNull UserProfileResponseDTO member,
                                          @NotBlank LocalDateTime createdAt,
                                          @NotBlank LocalDateTime modifiedAt,
                                          @NotBlank String role
) {
    public static TeamSpaceUserResponseDTO from(TeamSpaceUser teamSpaceUser) {
        return TeamSpaceUserResponseDTO.builder()
                .id(teamSpaceUser.getId())
                .member(UserProfileResponseDTO.from(teamSpaceUser.getMember()))
                .createdAt(teamSpaceUser.getCreatedAt())
                .modifiedAt(teamSpaceUser.getModifiedAt())
                .role(teamSpaceUser.getRole())
                .build();
    }
}
