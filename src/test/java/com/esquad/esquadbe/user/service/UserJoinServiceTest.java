package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.exception.UserIdException;
import com.esquad.esquadbe.exception.UserNicknameException;
import com.esquad.esquadbe.user.dto.UserJoinDTO;
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
class UserJoinServiceTest {

    @Autowired
    private UserJoinService userJoinService;

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
                .userId("testUser")
                .password("TestPasswd1234@")  // 평문 비밀번호 설정
                .userName("이정민")
                .email("testuser@example.com")
                .phoneNo("01012345678")
                .birthDay(LocalDate.parse("2000-09-09"))
                .address("부산광역시 어딘가")
                .nickname("testUser")
                .build();
    }

    @Test
    @DisplayName("이미 존재하는 아이디로 회원가입 시 UserIdException 발생")
    void userIdExist() {
        // Given
        Mockito.when(userRepository.existsByUserId(userJoinDTO.getUserId())).thenReturn(true);

        // When & Then
        assertThrows(UserIdException.class, () -> userJoinService.joinProcess(userJoinDTO));
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
        Mockito.when(userRepository.existsByUserId(userJoinDTO.getUserId())).thenReturn(false);
        Mockito.when(userRepository.existsByNickname(userJoinDTO.getNickname())).thenReturn(false);

        // When & Then
        userJoinService.joinProcess(userJoinDTO);

    }

    @Test
    @DisplayName("회원가입 시 닉네임이 비어 있을 경우 기본 닉네임이 설정된다")
    void defaultNicknameIfEmpty() {
        // Given
        UserJoinDTO emptyNicknameDTO = UserJoinDTO.builder()
                .userId("testUsers")
                .password("TestPasswd1234@")
                .userName("김경민")
                .email("testuser2@example.com")
                .phoneNo("01098765432")
                .birthDay(LocalDate.parse("2000-01-01"))
                .address("부산광역시 진흥원")
                .nickname("")  // 닉네임을 비워둠
                .build();

        Mockito.when(userRepository.existsByUserId(emptyNicknameDTO.getUserId())).thenReturn(false);
        Mockito.when(userRepository.existsByNickname(emptyNicknameDTO.getNickname())).thenReturn(false);

        // When
        userJoinService.joinProcess(emptyNicknameDTO);

        // Then
        assertNotNull(emptyNicknameDTO.getNickname());
        assertFalse(emptyNicknameDTO.getNickname().isEmpty());
    }
}
