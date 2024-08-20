package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamRepository teamRepository;


    @Override
    public Optional<TeamSpace> checkTeamName(String name) {
        return teamRepository.findByTeamName(name);
    }
}
