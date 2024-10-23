package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserUpdateTest {

    private User existingUser;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .email("testuser@example.com")
                .address("부산광역시 부산산업진행원")
                .phoneNumber("010-1234-5678")
                .nickname("testuser")
                .username("testuser")
                .birthDay(LocalDate.parse("2000-01-01"))
                .password("test1234@")
                .build();

        userUpdateDTO = UserUpdateDTO.builder()
                .email("test@example.com")
                .nickname("test")
                .build();
    }

    @Test
    void testToEntity_Success() {
        // when
        User updatedUser = userUpdateDTO.toEntity(userUpdateDTO, existingUser);

        // then
        assertThat(updatedUser.getId()).isEqualTo(existingUser.getId());
        assertThat(updatedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(updatedUser.getNickname()).isEqualTo("test");
        assertThat(updatedUser.getAddress()).isEqualTo(existingUser.getAddress());
        assertThat(updatedUser.getPhoneNumber()).isEqualTo(existingUser.getPhoneNumber());
        assertThat(updatedUser.getBirthDay()).isEqualTo(existingUser.getBirthDay());
        assertThat(updatedUser.getPassword()).isEqualTo(existingUser.getPassword());
        assertThat(updatedUser.getUsername()).isEqualTo(existingUser.getUsername());
    }
}
