package com.esquad.esquadbe.team.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @BeforeEach
    public void before() {
        TeamSpace team = TeamSpace.builder()
                    .teamName("blizard")
                    .build();
        lenient().when(teamRepository.save(any(TeamSpace.class))).thenReturn(team);
    }

    @Test
    @DisplayName("중복된 팀명으로 검색 테스트")
    void shouldAssertTrueWhenDuplicateName() {

        // given
        final String givenName = "blizard";
        given(teamRepository.existsByTeamName(givenName)).willReturn(true);

        // when - then
        boolean expectedResult = teamService.verifyTeamName(givenName);
        assertTrue(expectedResult);

        then(teamRepository).should(times(1)).existsByTeamName(givenName);
    }

    
}