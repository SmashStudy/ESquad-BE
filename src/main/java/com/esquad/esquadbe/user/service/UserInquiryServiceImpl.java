package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserGetResponseDTO;

import com.esquad.esquadbe.user.exception.UserInquiryException;
import com.esquad.esquadbe.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInquiryServiceImpl implements UserInquiryService {

    private final UserRepository userRepository;

    @Override
    public UserGetResponseDTO getUserById(final long id) {
        return userRepository.findById(id)
                .map(UserGetResponseDTO::of)
                .orElseThrow(UserInquiryException::new);
    }

    @Override
    public UserGetResponseDTO getUsername(final String username) {
        return userRepository.findByUsername(username)
                .map(UserGetResponseDTO::of)
                .orElseThrow(UserInquiryException::new);
    }
}