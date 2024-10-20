package com.esquad.esquadbe.team.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.esquad.esquadbe.global.exception.custom.DuplicateTeamException;
import com.esquad.esquadbe.user.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.team.service.TeamServiceImpl;
import com.esquad.esquadbe.user.repository.UserRepository;
import com.esquad.esquadbe.user.service.UserServiceTeamImpl;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class TeamRestControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @InjectMocks
    private UserServiceTeamImpl userService;

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
    @ExceptionHandler(DuplicateTeamException.class)
    public void shouldOccurErrorWhenDuplicateName() throws Exception {

        // given
        // final String givenName = "스메시";
        // given(teamService.verifyTeamName(givenName)).willThrow(new DuplicateTeamException(ErrorCode.DUPLICATE_NAME));

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
        // given(teamService.verifyTeamName(givenName)).willReturn(false);
        int actual = HttpStatus.OK.value();
        
        MvcResult result = mockmvc.perform(get("/team/new/{teamName}", givenName)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        int expected = result.getResponse().getStatus();
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("존재하지 않는 유저 닉네임 검색 시 Optional-null , 201 상태코드 반환 테스트")
    public void shouldReturnOptionalNullStat201() throws Exception {
        
        final ResponseDTO givenDTO = ResponseDTO.builder().nickname("gilgili").build();
        String content = new ObjectMapper().writeValueAsString(givenDTO);
        given(userService.searchUserByNick(givenDTO))
            .willReturn(Optional.ofNullable(null));

        ResultActions actions =
                mockmvc.perform(post("/team/new/search")
                        .contentType(MediaType.APPLICATION_JSON)  // 요청 본문이 JSON임을 지정
                        .content(content));                        // givenDTO의 JSON 표현을 요청 본문으로 전달

        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("존재하는 유저 닉네임 검색 시 Optional-DTO , 200 상태코드 반환 테스트")
    public void shouldReturnDTOStat200() throws Exception {

        final ResponseDTO givenDTO = ResponseDTO.builder().nickname("Zephyr").build();
        String content = new ObjectMapper().writeValueAsString(givenDTO);

        given(userService.searchUserByNick(givenDTO))
                .willReturn(Optional.of(givenDTO));

        ResultActions actions =
                mockmvc.perform(post("/team/new/search")
                        .contentType(MediaType.APPLICATION_JSON)  // 요청 본문이 JSON 임을 지정
                        .content(content)                        // givenDTO의 JSON 표현을 요청 본문으로 전달
                        .accept(MediaType.APPLICATION_JSON));   // 클라이언트가 JSON 응답을 기대

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(givenDTO.getNickname()));

    }
}