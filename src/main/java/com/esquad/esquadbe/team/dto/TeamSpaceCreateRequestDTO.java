package com.esquad.esquadbe.team.dto;


import java.util.List;

import com.esquad.esquadbe.team.entity.TeamSpace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Builder
public record TeamSpaceCreateRequestDTO(
        @NotBlank String teamName,
        @NotEmpty List<TeamSpaceUserRequestDTO> members
) {

   public TeamSpace to() {
      return TeamSpace.builder()
              .teamName(teamName)
              .members(members.stream().map(TeamSpaceUserRequestDTO::to).toList())
              .build();
   }
}
