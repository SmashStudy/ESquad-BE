package com.esquad.esquadbe.team.dto;

import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TeamSpaceUserCreateRequestDTO(
        @NotBlank TeamSpaceCreateRequestDTO teamSpace,
        @NotBlank UserResponseDTO member,
        @NotBlank String role
) {
    public TeamSpaceUser to() {
        return TeamSpaceUser.builder()
                .member(member.to())
                .role(role)
                .build();
    }
}
