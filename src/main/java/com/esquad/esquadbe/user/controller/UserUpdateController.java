package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.global.api.ApiResponseEntity;
import com.esquad.esquadbe.security.jwt.JwtUtil;
import com.esquad.esquadbe.user.dto.UserUpdateDTO;
import com.esquad.esquadbe.user.repository.UserRepository;
import com.esquad.esquadbe.user.service.UserUpdateService;
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
public class UserUpdateController {
    private final UserUpdateService userService;

    @PutMapping("/update")
    public ResponseEntity<ApiResponseEntity> updateUser(Authentication authentication, @RequestBody UserUpdateDTO userUpdateDTO) {
        // JWT에서 유저 ID를 추출
        Long userId = JwtUtil.getLoginId(authentication);

        // 유저 정보 수정
        var updatedUser = userService.updateUser(userId, userUpdateDTO);

        return ApiResponseEntity.successResponseEntity(updatedUser);
    }
}
