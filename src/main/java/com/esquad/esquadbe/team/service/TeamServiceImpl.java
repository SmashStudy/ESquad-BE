package com.esquad.esquadbe.team.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.esquad.esquadbe.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private static Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

    @Override
    public boolean verifyTeamName(String name) {
        logger.info("verifyTeamName()");
        return teamRepository.existsByTeamName(name);
    }
}
