package com.esquad.esquadbe.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.esquad.esquadbe.user.dto.ResponseDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceTeamImpl implements UserService  {

   private final UserRepository userRepository;   

   private static final Logger logger = LoggerFactory.getLogger(UserServiceTeamImpl.class);

      @Override
      public Optional<ResponseDTO> searchUserByNick(final ResponseDTO dto) {
         logger.info("searchUserByNick()");
         Optional<User> user = userRepository.findByNickname(dto.getNickname());
         return user.flatMap(User::toResponseDTO);
      }

}
