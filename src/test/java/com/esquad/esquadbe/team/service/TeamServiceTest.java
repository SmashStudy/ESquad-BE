package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@Transactional
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {

        //TeamSpace teamSpace = TeamSpace.builder().teamName("스매시").

    }

    @Test
    void checkTeamName() {
    }
}