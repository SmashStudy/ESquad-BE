package com.esquad.esquadbe.team.controller;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.esquad.esquadbe.team.service.TeamService;

@SpringBootTest
@WebMvcTest(TeamRestController.class)
@AutoConfigureMockMvc
class TeamRestControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @MockBean
    private TeamService teamService;

    @Test
    @DisplayName("/team/new 로 접속하면 팀 스페이스를 생성하는 페이지가 나와야 한다.")
    public void shoudReturnCreatePage() throws Exception {
        
        mockmvc.perform(
            MockMvcRequestBuilders.get("/team/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("new")));
    }

    @Test
    @DisplayName("팀 스페이스명의 유효성을 검사한다. DB에 있는 이름과 중복될 경우 실패해야 한다.")
    public void shouldFailWhenExists() {
        // given
        final String givenName = "스메시";

        // when
        

        // them
    }
}