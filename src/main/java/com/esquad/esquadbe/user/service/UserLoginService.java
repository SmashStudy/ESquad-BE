package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserLoginRequestDTO;
import com.esquad.esquadbe.user.dto.UserLoginResponseDTO;

public interface UserLoginService {
    UserLoginResponseDTO login(final UserLoginRequestDTO userLoginRequestDTO);
}
