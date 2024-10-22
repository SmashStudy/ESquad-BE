package com.esquad.esquadbe.user.service;


import com.esquad.esquadbe.user.dto.UserGetResponseDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.global.exception.custom.user.UserInquiryException;
import com.esquad.esquadbe.user.impl.UserGetServiceImpl;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserGetTest {

    @InjectMocks
    private UserGetServiceImpl userGetService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_Success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("testUser")
                .password("test1234@")
                .nickname("testNickname")
                .phoneNumber("01012345678")
                .email("test@example.com")
                .address("testAddress")
                .birthDay(LocalDate.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserGetResponseDTO response = userGetService.getUserById(userId);

        // then
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("testUser", response.username());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_Fail_UserNotFound() {
        // given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserInquiryException.class, () -> userGetService.getUserById(userId));
    }

    @Test
    void getUserByUserId_Success() {
        // given
        String username = "testUser";
        User user = User.builder()
                .id(1L)
                .username(username)
                .password("test1234@")
                .nickname("testNickname")
                .phoneNumber("01012345678")
                .email("test@example.com")
                .address("testAddress")
                .birthDay(LocalDate.now())
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // when
        UserGetResponseDTO response = userGetService.getUserByUserId(username);

        // then
        assertNotNull(response);
        assertEquals(username, response.username());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByUserId_Fail_UserNotFound() {
        // given
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserInquiryException.class, () -> userGetService.getUserByUserId(username));
    }
}