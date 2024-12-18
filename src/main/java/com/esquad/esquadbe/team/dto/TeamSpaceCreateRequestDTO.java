package com.esquad.esquadbe.team.dto;


import java.util.List;

import com.esquad.esquadbe.team.entity.TeamSpace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
public record TeamSpaceCreateRequestDTO(
        @NotBlank @Size(min = 2, max = 20, message = "팀스페이스명은 2~20자 사이여야 합니다.")
        String teamName,
        @NotEmpty
        @Size(min = 4, max = 11, message = "크루는 4명이상 11명 이하로 구성되어야 합니다.")
        List<TeamSpaceUserCreateRequestDTO> members
) {

   public TeamSpace to() {
      return TeamSpace.builder()
              .teamName(teamName)
              .members(members.stream().map(TeamSpaceUserCreateRequestDTO::to).toList())
              .build();
   }
}
