package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.dto.UserDTO;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username);
        return UserDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDay(user.getBirthDay())
                .address(user.getAddress())
                .build();
    }
}
