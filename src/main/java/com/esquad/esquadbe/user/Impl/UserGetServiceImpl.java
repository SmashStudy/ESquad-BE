package com.esquad.esquadbe.user.Impl;

import com.esquad.esquadbe.user.dto.UserGetResponseDTO;
import com.esquad.esquadbe.user.exception.UserException;
import com.esquad.esquadbe.user.exception.UserExceptionResult;
import com.esquad.esquadbe.user.repository.UserRepository;

import com.esquad.esquadbe.user.service.UserGetService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserGetServiceImpl implements UserGetService {

    private final UserRepository userRepository;

    /**
     * 사용자 idx를 이용해 사용자 정보 조회
     *
     * @param id 사용자 idx
     * @return 사용자 정보 UserGetResponseDTO
     */

    @Override
    public UserGetResponseDTO getUserById(final long id) {
        return userRepository.findById(id)
                .map(UserGetResponseDTO::of)
                .orElseThrow(() -> new UserException(UserExceptionResult.NOT_EXISTS));
    }

    /**
     * 사용자 id를 이용해 사용자 정보 조회
     *
     * @param username 사용자 id
     * @return 사용자 정보 UserGetResponseDTO
     */

    @Override
    public UserGetResponseDTO getUserByUserId(final String username) {
        return userRepository.findByUsername(username)
                .map(UserGetResponseDTO::of)
                .orElseThrow(() -> new UserException(UserExceptionResult.NOT_EXISTS));
    }
}
