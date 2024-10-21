package com.esquad.esquadbe.user.Impl;

import com.esquad.esquadbe.user.dto.UserUpdatePasswordDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import com.esquad.esquadbe.user.repository.UserRepository;
import com.esquad.esquadbe.user.service.UserPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPasswordServiceImpl implements UserPasswordService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User updateUser(Long userId, UserUpdatePasswordDTO userUpdatePasswordDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(userUpdatePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = bCryptPasswordEncoder.encode(userUpdatePasswordDTO.getNewPassword());
        User updatedUser = userUpdatePasswordDTO.toEntity(encodedNewPassword, user);

        return userRepository.save(updatedUser);
    }
}
