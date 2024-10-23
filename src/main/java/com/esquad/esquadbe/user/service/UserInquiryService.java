package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserGetResponseDTO;

public interface UserInquiryService {
    UserGetResponseDTO getUserById(final long id);

    UserGetResponseDTO getUsername(final String username);
}