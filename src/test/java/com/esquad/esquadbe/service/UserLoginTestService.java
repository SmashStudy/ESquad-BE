package com.esquad.esquadbe.service;

import com.esquad.esquadbe.user.config.SecurityConfig;
import com.esquad.esquadbe.user.config.UserDetailsImpl;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserLoginTestService {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .username("testuser")
                .password(new SecurityConfig().bCryptPasswordEncoder().encode("password"))
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
    }

    @Test
    @DisplayName("로그인 페이지 조회")
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string("login"));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void testSuccessfulLogin() throws Exception {
        mockMvc.perform(formLogin("/loginProc")
                        .user("username", "testuser")
                        .password("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("testuser"));
    }


    @Test
    @DisplayName("로그인 실패 테스트")
    public void testFailedLogin() throws Exception {
        mockMvc.perform(formLogin("/loginProc")
                        .user("username", "testuser")
                        .password("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("유저 정보 가져오가")
    public void testGetUserProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string("testuser"));
    }
}
