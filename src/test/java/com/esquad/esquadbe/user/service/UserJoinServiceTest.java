package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserJoinServiceTest {

    @Autowired
    private UserJoinService userJoinService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalValidatorFactoryBean validator;

    private UserJoinDTO userJoinDTO;

    @BeforeEach
    void setUp() {
        userJoinDTO = UserJoinDTO.builder()
                .userId("testUser")
                .password("TestPasswd1234@")
                .userName("이정민")
                .email("testuser@example.com")
                .phoneNo("01012345678")
                .birthDay(LocalDate.parse("2000-09-09"))
                .address("부산광역시 남구 수영로 325번길 61")
                .nickname("testUser")
                .build();

        // 테스트 환경을 깨끗하게 초기화
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("이미 존재하는 아이디로 회원가입 시 예외가 발생한다")
    void userIdExist() throws Exception {
        // Given
        userRepository.save(userJoinDTO.toEntity());

        // When
        boolean userIdExist = userRepository.existsByUserId(userJoinDTO.getUserId());
        assertTrue(userIdExist);

        // Then
        Exception e = assertThrows(Exception.class, () -> userJoinService.joinProcess(userJoinDTO));
        assertEquals("이미 존재하는 아이디입니다.", e.getMessage());
    }

    @Test
    @DisplayName("이미 존재하는 닉네임으로 회원가입 시 예외가 발생한다")
    void nicknameExists() throws Exception {
        // Given:
        userRepository.save(userJoinDTO.toEntity());

        // 다른 아이디 생성
        UserJoinDTO newUserId = UserJoinDTO.builder()
                .userId("testNewUser")
                .password("TestPasswd1234@")
                .userName("이정민")
                .email("testuser@example.com")
                .phoneNo("01012345678")
                .birthDay(LocalDate.parse("2000-09-09"))
                .address("부산광역시 남구 수영로 325번길 61")
                .nickname("testUser")
                .build();

        // When
        boolean nicknameExist = userRepository.existsByNickname(userJoinDTO.getNickname());
        assertTrue(nicknameExist);
        // Then
        Exception e = assertThrows(Exception.class, () -> userJoinService.joinProcess(newUserId));
        assertEquals("이미 존재하는 닉네임입니다.", e.getMessage());
    }

    @Test
    @DisplayName("회원가입이 정상적으로 진행된다")
    void successJoinUser() throws Exception {
        // When
        userJoinService.joinProcess(userJoinDTO);
        // Then
        boolean userExist = userRepository.existsByUserId("testUser");
        assertTrue(userExist);

    }

    @Test
    @DisplayName("정규식이 옳바르지 않은 상태")
    void failJoinUserId() throws Exception {
        UserJoinDTO userJoinDTO = UserJoinDTO.builder()
                .userId("testUser1")
                .password("TestPasswd1234@")
                .userName("이정민")
                .email("testuser@example.com")
                .phoneNo("01012345678")
                .birthDay(LocalDate.parse("2000-09-09"))
                .address("부산광역시 남구 수영로 325번길 61")
                .nickname("testUser")
                .build();
        // ConstraintViolationException 유효성 검사 예외처리
        assertThrows(ConstraintViolationException.class, () -> userJoinService.joinProcess(userJoinDTO));
    }
}
