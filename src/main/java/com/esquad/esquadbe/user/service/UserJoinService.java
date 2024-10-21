package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.exception.UserIdException;
import com.esquad.esquadbe.user.exception.UserNicknameException;
import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserUsernameException;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserJoinDTO userJoinDTO) {
        if (userRepository.existsByUsername(userJoinDTO.getUsername())) {
            throw new UserUsernameException();
        }

        if (userRepository.existsByNickname(userJoinDTO.getNickname())) {
            throw new UserNicknameException();
        }

        if(userJoinDTO.getNickname().isEmpty()) {
            userJoinDTO.setDefaultNickname();
        }

        User user = userJoinDTO.toEntity(bCryptPasswordEncoder);

        userRepository.save(user);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
