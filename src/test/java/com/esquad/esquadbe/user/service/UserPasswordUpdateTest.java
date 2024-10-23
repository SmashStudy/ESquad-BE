package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdatePasswordDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class UserPasswordUpdateTest {

    @Autowired
    private UserPasswordUpdateServiceImpl userPasswordService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        Mockito.when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
    }

    @Test
    @DisplayName("비밀번호 업데이트가 성공적으로 진행된다")
    void updatePassword_Success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("testUser")
                .password("currentPassword")
                .build();

        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .currentPassword("currentPassword")
                .newPassword("newPassword")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(userUpdatePasswordDTO.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User updatedUser = userPasswordService.updateUser(userId, userUpdatePasswordDTO);

        // then
        assertNotNull(updatedUser);
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 인해 비밀번호 업데이트 실패")
    void updatePassword_Fail_UserNotFound() {
        // given
        Long userId = 1L;
        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .currentPassword("currentPassword")
                .newPassword("newPassword")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userPasswordService.updateUser(userId, userUpdatePasswordDTO));
    }

    @Test
    @DisplayName("현재 비밀번호가 일치하지 않아 비밀번호 업데이트 실패")
    void updatePassword_Fail_IncorrectCurrentPassword() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("testUser")
                .password("currentPassword")
                .build();

        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .currentPassword("wrongPassword")
                .newPassword("newPassword")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(userUpdatePasswordDTO.getCurrentPassword(), user.getPassword())).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userPasswordService.updateUser(userId, userUpdatePasswordDTO));
    }
}
