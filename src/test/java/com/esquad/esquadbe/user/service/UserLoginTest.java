package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.redis.RedisUtil;
import com.esquad.esquadbe.security.jwt.JwtProvider;
import com.esquad.esquadbe.user.dto.UserGetResponseDTO;
import com.esquad.esquadbe.user.dto.UserLoginRequestDTO;
import com.esquad.esquadbe.user.dto.UserLoginResponseDTO;
import com.esquad.esquadbe.user.exception.UserLoginException;
import com.esquad.esquadbe.user.impl.UserGetServiceImpl;
import com.esquad.esquadbe.user.impl.UserLoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class UserLoginTest {

    @Autowired
    private UserLoginServiceImpl userLoginService;

    @MockBean
    private UserGetServiceImpl userGetService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeEach
    void setUp() {
        // Mock PasswordEncoder
        Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn("encryptedPassword");
    }

    @Test
    @DisplayName("로그인 성공 시 액세스 및 리프레시 토큰이 생성된다")
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
    @DisplayName("비밀번호가 일치하지 않을 경우 로그인 실패")
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
        assertThrows(UserLoginException.class, () -> userLoginService.login(loginRequestDTO));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 인해 로그인 실패")
    void login_Fail_UserNotFound() {
        // given
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("testtest", "wrongPassword");

        when(userGetService.getUserByUserId(loginRequestDTO.getUsername())).thenThrow(new UserLoginException());

        // when & then
        assertThrows(UserLoginException.class, () -> userLoginService.login(loginRequestDTO));
    }
}
