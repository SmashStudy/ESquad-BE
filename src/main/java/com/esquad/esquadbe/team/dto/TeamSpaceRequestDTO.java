package com.esquad.esquadbe.team.dto;


import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;


@Builder
public record TeamSpaceRequestDTO(
        Long id,
        @NotBlank String teamName,
        @NotEmpty List<TeamSpaceUserCreateRequestDTO> members,
        String description
) {

   public TeamSpace to() {
      return TeamSpace.builder()
              .id(id)
              .teamName(teamName)
              .members(members.stream().map(TeamSpaceUserCreateRequestDTO::to).toList())
              .description(description)
              .build();
   }
}
