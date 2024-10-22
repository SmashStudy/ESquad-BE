package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository userRepository;

    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        user = userUpdateDTO.toEntity(userUpdateDTO,user);

        return userRepository.save(user);
    }
}