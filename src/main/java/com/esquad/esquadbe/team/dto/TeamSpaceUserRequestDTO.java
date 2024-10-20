package com.esquad.esquadbe.team.dto;

import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
public record TeamSpaceUserRequestDTO (
        Long id,
        @NotBlank TeamSpaceCreateRequestDTO teamSpace,
        @NotBlank UserResponseDTO member,
        @NotBlank String role
) {
    public TeamSpaceUser to() {
        return TeamSpaceUser.builder()
                .id(id)
                .member(member.to())
                .role(role)
                .build();
    }
}
