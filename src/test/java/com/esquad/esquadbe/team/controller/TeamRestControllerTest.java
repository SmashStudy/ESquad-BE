package com.esquad.esquadbe.team.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.esquad.esquadbe.global.handle.DuplicateNameException;
import com.esquad.esquadbe.global.handle.ErrorCode;
import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.team.service.TeamServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
class TeamRestControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;


    @Test
    @DisplayName("/team/new 로 접속하면 팀 스페이스를 생성하는 페이지가 나와야 한다.")
    public void shouldReturnCreatePage() throws Exception {
        
        mockmvc.perform(
            MockMvcRequestBuilders.get("/team/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("new")));
    }

    @Test
    @DisplayName("팀 스페이스명의 유효성을 검사한다. DB에 있는 이름과 중복될 경우 에러를 던진다.")
    @ExceptionHandler(DuplicateNameException.class)
    public void shouldOccurErrorWhenDuplicateName() throws Exception {

        // given
        final String givenName = "스메시";
        given(teamService.verifyTeamName(givenName)).willThrow(new DuplicateNameException(ErrorCode.DUPLICATE_NAME));

        // when-then
        MvcResult result = mockmvc.perform(get("/team/new/{teamName}", givenName)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print()) 
            .andReturn();

        int expected = result.getResponse().getStatus();
        assertEquals(403, expected);
    }

    @Test
    @DisplayName("팀 스페이스명의 유효성을 검사한다. 중복되지 않는 경우 200 상태코드를 보인다.")
    public void shouldStatusIsOkWhenUniqueName() throws Exception {

        final String givenName = "gilgili";
        given(teamService.verifyTeamName(givenName)).willReturn(false);
        int actual = HttpStatus.OK.value();
        
        MvcResult result = mockmvc.perform(get("/team/new/{teamName}", givenName)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        int expected = result.getResponse().getStatus();
        assertEquals(actual, expected);
    }
}