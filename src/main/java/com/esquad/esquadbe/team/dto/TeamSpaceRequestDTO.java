package com.esquad.esquadbe.team.dto;


import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamSpaceRequestDTO {

   private Long id;
   private String teamName;
   private List<TeamSpaceUser> members;
   private List<StudyPage> studyPages;
   private List<BookQnaBoard> qnaBoards;
   private String description;

   public static TeamSpaceResponseDTO toDTO(TeamSpace teamSpace) {
      return TeamSpaceResponseDTO.builder()
               .id(teamSpace.getId())
               .teamName(teamSpace.getTeamName())
               .members(teamSpace.getMembers())
               .studyPages(teamSpace.getStudyPages())
               .qnaBoards(teamSpace.getQnaBoards())
               .build();
   }

   public TeamSpace toEntity() {
      return TeamSpace.builder()
               .teamName(teamName)
               .members(members)
               .studyPages(studyPages)
               .qnaBoards(qnaBoards)
               .description(description)
               .build();
   }
}
