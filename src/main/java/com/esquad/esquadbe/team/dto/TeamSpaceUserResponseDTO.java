package com.esquad.esquadbe.team.dto;

import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record TeamSpaceUserResponseDTO(   // 팀스페이스 멤버정보
                                          @NotNull Long id,
                                          @ToString.Exclude
                                          @NotNull TeamSpaceResponseDTO teamSpace,
                                          @ToString.Exclude
                                          @NotNull UserResponseDTO user,
                                          @NotBlank String role
) {
    public static TeamSpaceUserResponseDTO from(TeamSpaceUser teamSpaceUser) {
        return TeamSpaceUserResponseDTO.builder()
                .id(teamSpaceUser.getId())
                .teamSpace(TeamSpaceResponseDTO.from(teamSpaceUser.getTeamSpace()))
                .user(UserResponseDTO.from(teamSpaceUser.getMember()))
                .role(teamSpaceUser.getRole())
                .build();
    }
}
