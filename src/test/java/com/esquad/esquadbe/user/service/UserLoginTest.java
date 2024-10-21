package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.redis.RedisUtil;
import com.esquad.esquadbe.security.jwt.JwtProvider;
import com.esquad.esquadbe.user.Impl.UserGetServiceImpl;
import com.esquad.esquadbe.user.Impl.UserLoginServiceImpl;
import com.esquad.esquadbe.user.dto.UserGetResponseDTO;
import com.esquad.esquadbe.user.dto.UserLoginRequestDTO;
import com.esquad.esquadbe.user.dto.UserLoginResponseDTO;
import com.esquad.esquadbe.user.exception.LoginException;
import com.esquad.esquadbe.user.exception.LoginExceptionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserLoginTest {

    @InjectMocks
    private UserLoginServiceImpl userLoginService;

    @Mock
    private UserGetServiceImpl userGetService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RedisUtil redisUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        // given
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("testUser", "password");
        UserGetResponseDTO userInfo = UserGetResponseDTO.builder()
                .id(1L)
                .username("testUser")
                .password("testPassword")
                .nickname("testNickname")
                .phoneNumber("01012345678")
                .email("test@example.com")
                .address("testAddress")
                .birthDay(LocalDate.now())
                .build();

        when(userGetService.getUserByUserId(loginRequestDTO.getUsername())).thenReturn(userInfo);
        when(bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), userInfo.password())).thenReturn(true);
        when(jwtProvider.generateAccessToken(userInfo.id())).thenReturn("accessToken");
        when(jwtProvider.generateRefreshToken(userInfo.id())).thenReturn("refreshToken");

        // when
        UserLoginResponseDTO response = userLoginService.login(loginRequestDTO);

        // then
        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(redisUtil, times(1)).set(any(), any(), anyInt());
    }

    @Test
    void login_Fail_InvalidPassword() {
        // given
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("testUser", "wrongPassword");
        UserGetResponseDTO userInfo = UserGetResponseDTO.builder()
                .id(1L)
                .username("testUser")
                .password("hashedPassword")
                .nickname("testNickname")
                .phoneNumber("1234567890")
                .email("test@example.com")
                .address("testAddress")
                .birthDay(LocalDate.now())
                .build();

        when(userGetService.getUserByUserId(loginRequestDTO.getUsername())).thenReturn(userInfo);
        when(bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), userInfo.password())).thenReturn(false);

        // when & then
        assertThrows(LoginException.class, () -> userLoginService.login(loginRequestDTO));
    }

    @Test
    void login_Fail_UserNotFound() {
        // given
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("testtest", "wrongPassword");

        when(userGetService.getUserByUserId(loginRequestDTO.getUsername())).thenThrow(new LoginException(LoginExceptionResult.NOT_CORRECT));

        // when & then
        assertThrows(LoginException.class, () -> userLoginService.login(loginRequestDTO));
    }
}
