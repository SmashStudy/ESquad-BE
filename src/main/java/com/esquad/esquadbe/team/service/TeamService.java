package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.dto.TeamSpaceCreateRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceResponseDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceUserResponseDTO;
import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.validation.Valid;

import java.util.List;

public interface TeamService {

    void verifyTeamName(String teamName);
    TeamSpace createTeam(TeamSpaceCreateRequestDTO teamDTO);
    TeamSpaceResponseDTO updateProfile(@Valid TeamSpaceRequestDTO teamSpaceRequestDTO);
    List<TeamSpaceUserResponseDTO> getCrewProfile(Long teamId);
    TeamSpaceResponseDTO getTeamProfile(Long teamSpaceRequestDTO);
    void deleteTeamSpace(Long teamId);
}
