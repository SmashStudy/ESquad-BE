package com.esquad.esquadbe.user.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
// import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito.BDDMyOngoingStubbing;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.esquad.esquadbe.user.dto.ResponseDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

   @Mock
   private UserRepository userRepository;

   @InjectMocks
   private UserServiceTeamImpl userService;

   @Test
   @DisplayName("닉네임 유효성 테스트 - 검색결과가 없는 경우")
   void shouldReturnOptionalNullWhenNickNotExists() {

      final ResponseDTO givenDTO = ResponseDTO.builder().nickname("hulekji").build();
      given(userRepository.findByNickname(givenDTO.getNickname()))
              .willReturn(Optional.ofNullable(null));
      Optional<ResponseDTO> actualUser = Optional.ofNullable(null);

      Optional<ResponseDTO> expectedUser = userService.searchUserByNick(givenDTO);

       assertTrue(expectedUser.isEmpty());
   }

   @Test
   @DisplayName("닉네임 유효성 테스트 - 검색결과가 있는 경우")
   void shouldReturnOptionalDTOWhenNickExists() {

      final ResponseDTO givenDTO = ResponseDTO.builder().nickname("Zephyr").build();
      given(userRepository.findByNickname(givenDTO.getNickname()))
              .willReturn(Optional.of(User.builder().nickname("Zephyr").build()));
      Optional<ResponseDTO> actualUser = Optional.of(User.builder().nickname("Zephyr").build())
                                                   .flatMap(User::toResponseDTO);

      Optional<ResponseDTO> expectedUser = userService.searchUserByNick(givenDTO);

      assertTrue(expectedUser.isPresent());
      assertEquals(expectedUser.get().toString(), actualUser.get().toString());
   }

}
