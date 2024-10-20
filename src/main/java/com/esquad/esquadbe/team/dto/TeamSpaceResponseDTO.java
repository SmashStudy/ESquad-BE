package com.esquad.esquadbe.team.dto;

import java.util.List;
import lombok.Builder;

import com.esquad.esquadbe.team.entity.TeamSpace;


@Builder
public record TeamSpaceResponseDTO (
     Long id,
     String teamName,
     List<TeamSpaceUserResponseDTO> members,
     // List<StudyPage> studyPages,
     // List<BookQnaBoard> qnaBoards,
     String description
) {

   public static TeamSpaceResponseDTO from(TeamSpace teamSpace) {
         return TeamSpaceResponseDTO.builder()
                  .id(teamSpace.getId())
                  .teamName(teamSpace.getTeamName())
                  .members(teamSpace.getMembers().stream()
                          .map(TeamSpaceUserResponseDTO::from)
                          .toList())
                  // .studyPages(teamSpace.getStudyPages())
                  // .qnaBoards(teamSpace.getQnaBoards())
                 .description(teamSpace.getDescription())
                  .build();
   }

}
