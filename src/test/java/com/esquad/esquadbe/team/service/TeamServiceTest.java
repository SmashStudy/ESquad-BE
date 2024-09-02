package com.esquad.esquadbe.team.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.esquad.esquadbe.team.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    @DisplayName("중복 팀명 검색 시 예외 발생 ")
    void shouldFailWhenNameExists() {
        // final String givenName = "스메시";
        // boolean expectedResult = teamService.checkTeamName(givenName);
        // assertTrue(expectedResult);
    }
}