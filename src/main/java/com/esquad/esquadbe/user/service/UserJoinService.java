package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserJoinDTO userJoinDTO) throws Exception {
        if (userRepository.existsByUserId(userJoinDTO.getUserId())) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        if (userRepository.existsByNickname(userJoinDTO.getNickname())) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        userJoinDTO.emptyNickname();
        userRepository.save(userJoinDTO.toEntity());
        userJoinDTO.setPassword(bCryptPasswordEncoder.encode(userJoinDTO.getPassword()));
    }
}
