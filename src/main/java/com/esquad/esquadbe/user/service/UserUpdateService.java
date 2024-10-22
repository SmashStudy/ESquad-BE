package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.entity.User;

public interface UserUpdateService {
    User updateUser(Long userId, UserUpdateDTO userUpdateDTO);
}