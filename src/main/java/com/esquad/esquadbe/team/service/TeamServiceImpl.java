package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.controller.TeamRestController;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private static Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

    @Override
    public boolean checkTeamName(String name) {

        logger.info("checkTeamName()");

        return teamRepository.existsByTeamName(name);
    }
}
