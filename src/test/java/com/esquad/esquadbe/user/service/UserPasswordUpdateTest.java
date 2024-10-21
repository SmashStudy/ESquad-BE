package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.Impl.UserPasswordServiceImpl;
import com.esquad.esquadbe.user.dto.UserUpdatePasswordDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPasswordUpdateTest {

    @InjectMocks
    private UserPasswordServiceImpl userPasswordService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
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
        when(bCryptPasswordEncoder.encode(userUpdatePasswordDTO.getNewPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User updatedUser = userPasswordService.updateUser(userId, userUpdatePasswordDTO);

        // then
        assertNotNull(updatedUser);
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
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
