package com.esquad.esquadbe.user.service;



import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.exception.UserNicknameException;
import com.esquad.esquadbe.user.exception.UserUsernameException;
import com.esquad.esquadbe.user.impl.UserJoinServiceImpl;
import com.esquad.esquadbe.user.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Transactional
class UserJoinTest {

    @Autowired
    private UserJoinServiceImpl userJoinService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserJoinDTO userJoinDTO;

    @BeforeEach
    void setUp() {
        // Mock PasswordEncoder
        Mockito.when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encryptedPassword");

        userJoinDTO = UserJoinDTO.builder()
                .username("testuser")
                .password("testuser1234@")
                .email("testuser@example.com")
                .phoneNumber("01012345678")
                .birthDay(LocalDate.parse("2000-09-09"))
                .address("부산광역시")
                .nickname("testuser")
                .build();
    }

    @Test
    @DisplayName("이미 존재하는 아이디로 회원가입 시 UserIdException 발생")
    void userIdExist() {
        // Given
        Mockito.when(userRepository.existsByUsername(userJoinDTO.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(UserUsernameException.class, () -> userJoinService.joinProcess(userJoinDTO));
    }

    @Test
    @DisplayName("이미 존재하는 닉네임으로 회원가입 시 UserNicknameException 발생")
    void nicknameExists() {
        // Given
        Mockito.when(userRepository.existsByNickname(userJoinDTO.getNickname())).thenReturn(true);

        // When & Then
        assertThrows(UserNicknameException.class, () -> userJoinService.joinProcess(userJoinDTO));
    }

    @Test
    @DisplayName("회원가입이 정상적으로 진행된다")
    void successJoinUser() {
        // Given
        Mockito.when(userRepository.existsByUsername(userJoinDTO.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByNickname(userJoinDTO.getNickname())).thenReturn(false);

        // When & Then
        userJoinService.joinProcess(userJoinDTO);

    }
}