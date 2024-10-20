package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.global.api.ApiResponseEntity;
import com.esquad.esquadbe.security.jwt.JwtUtil;
import com.esquad.esquadbe.user.dto.UserUpdatePasswordDTO;
import com.esquad.esquadbe.user.service.UserPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserUpdatePasswordController {
    private final UserPasswordService userUpdateService;

    @PutMapping("/password")
    public ResponseEntity<ApiResponseEntity> updatePassword(Authentication authentication, @Valid @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO) {
        Long userId = JwtUtil.getLoginId(authentication);
        var updatedUserPassword = userUpdateService.updateUser(userId, userUpdatePasswordDTO);
        return ApiResponseEntity.successResponseEntity(updatedUserPassword);

    }
}
