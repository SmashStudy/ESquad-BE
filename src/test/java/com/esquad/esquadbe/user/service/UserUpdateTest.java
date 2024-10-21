package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserUpdateTest {

    private User existingUser;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        // 기존 User 엔티티 생성 (원본 데이터)
        existingUser = User.builder()
                .id(1L)
                .email("testuser@example.com")
                .address("부산광역시 부산산업진흥원")
                .phoneNumber("010-1234-5678")
                .nickname("testuser")
                .username("testuser")
                .birthDay(LocalDate.parse("2000-01-01"))
                .password("test1234@")
                .build();

        // 업데이트할 데이터가 담긴 DTO 생성
        userUpdateDTO = UserUpdateDTO.builder()
                .email("test@example.com")  // 이메일만 변경
                .nickname("test")  // 닉네임만 변경
                .build();
    }

    @Test
    void testToEntity_Success() {
        // when: 기존 유저와 DTO 데이터를 병합하여 새 엔티티 생성
        User updatedUser = userUpdateDTO.toEntity(userUpdateDTO, existingUser);

        // then: 업데이트된 필드와 유지된 필드를 검증
        assertThat(updatedUser.getId()).isEqualTo(existingUser.getId());
        assertThat(updatedUser.getEmail()).isEqualTo("test@example.com");  // 이메일이 업데이트됨
        assertThat(updatedUser.getNickname()).isEqualTo("test");  // 닉네임이 업데이트됨
        assertThat(updatedUser.getAddress()).isEqualTo(existingUser.getAddress());  // 주소는 유지됨
        assertThat(updatedUser.getPhoneNumber()).isEqualTo(existingUser.getPhoneNumber());  // 전화번호 유지
        assertThat(updatedUser.getBirthDay()).isEqualTo(existingUser.getBirthDay());  // 생일 유지
        assertThat(updatedUser.getPassword()).isEqualTo(existingUser.getPassword());  // 비밀번호 유지
        assertThat(updatedUser.getUsername()).isEqualTo(existingUser.getUsername());  // 유저명 유지
    }
}
