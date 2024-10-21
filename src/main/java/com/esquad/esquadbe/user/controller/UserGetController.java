package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.global.api.ApiResponseEntity;
import com.esquad.esquadbe.security.jwt.JwtUtil;
import com.esquad.esquadbe.user.service.UserGetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserGetController {

    private final UserGetService userGetService;

    @GetMapping("/inquiries")
    public ResponseEntity<ApiResponseEntity> dashboard(Authentication authentication) {
        // 사용자 정보 조회
        var result = userGetService.getUserById(JwtUtil.getLoginId(authentication));

        return ApiResponseEntity.successResponseEntity(result);
    }
}
