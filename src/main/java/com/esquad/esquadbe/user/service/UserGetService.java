package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserGetResponseDTO;

public interface UserGetService {
    UserGetResponseDTO getUserById(final long id);

    UserGetResponseDTO getUsername(final String username);
}