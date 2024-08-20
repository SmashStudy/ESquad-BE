package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.entity.TeamSpace;

import java.util.Optional;

public interface TeamService {

    public Optional<TeamSpace> checkTeamName(String teamName);
}
