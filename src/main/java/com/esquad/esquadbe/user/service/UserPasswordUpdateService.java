package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdatePasswordDTO;
import com.esquad.esquadbe.user.entity.User;

public interface UserPasswordUpdateService {
    User updateUser(Long userId, UserUpdatePasswordDTO userUpdatePasswordDTO);
}