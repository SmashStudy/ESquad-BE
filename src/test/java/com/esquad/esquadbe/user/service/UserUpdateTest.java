package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.entity.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc를 자동으로 설정하여 사용 가능하게 함
public class UserUpdateTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc는 자동 설정을 통해 주입됩니다.

    @MockBean
    private UserUpdateService userService;

    @MockBean
    private Authentication authentication;  // 인증도 MockBean으로 주입

    @Autowired
    private ObjectMapper objectMapper;  // JSON 직렬화를 위한 ObjectMapper

    private UserUpdateDTO updateDTO;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        updateDTO = UserUpdateDTO.builder()
                .email("newEmail@example.com")
                .address("newAddress")
                .phoneNumber("010-1234-5678")
                .nickname("newNickname")
                .build();

        updatedUser = User.builder()
                .id(1L)
                .username("newUsername")
                .email("newEmail@example.com")
                .address("newAddress")
                .phoneNumber("010-1234-5678")
                .nickname("newNickname")
                .build();
    }

    @Test
    void testUpdateUser() throws Exception {
        // Mock the authentication and service layer
        Mockito.when(authentication.getName()).thenReturn("1");
        Mockito.when(userService.updateUser(anyLong(), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .principal(authentication)) // Pass mock authentication
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUsername")) // adjust path if needed
                .andExpect(jsonPath("$.email").value("newEmail@example.com"));
    }
}