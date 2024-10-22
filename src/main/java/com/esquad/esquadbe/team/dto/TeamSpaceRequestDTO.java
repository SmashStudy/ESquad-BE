package com.esquad.esquadbe.team.dto;


import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;


@Builder
public record TeamSpaceRequestDTO(
        @NotBlank
        Long id,
        @NotBlank @Size(min = 2, max = 20, message = "팀스페이스명은 2~20자 사이여야 합니다.")
        String teamName,
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
