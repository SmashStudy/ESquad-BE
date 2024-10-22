package com.esquad.esquadbe.user.impl;

import com.esquad.esquadbe.redis.RedisUtil;
import com.esquad.esquadbe.user.dto.UserLoginRequestDTO;
import com.esquad.esquadbe.user.dto.UserLoginResponseDTO;

import com.esquad.esquadbe.user.dto.UserGetResponseDTO;



import com.esquad.esquadbe.security.jwt.JwtProvider;

import com.esquad.esquadbe.user.exception.UserLoginException;
import com.esquad.esquadbe.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserGetServiceImpl userGetService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtProvider jwtProvider;

    private final RedisUtil redisUtil;

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    @Override
    @Transactional
    public UserLoginResponseDTO login(final UserLoginRequestDTO loginRequestDTO) {
        // 사용자 정보 조회
        UserGetResponseDTO userInfo = userGetService.getUserByUserId(loginRequestDTO.getUsername());

        // password 일치 여부 체크
        if(!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), userInfo.password()))
            throw new UserLoginException();

        // jwt 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(userInfo.id());

        String existingRefreshToken = findRefreshTokenByUserId(userInfo.id());
        if (existingRefreshToken != null) {
            redisUtil.delete(existingRefreshToken);
        }


        // refresh token 생성 후 저장
        String refreshToken = jwtProvider.generateRefreshToken(userInfo.id());
        redisUtil.set(REFRESH_TOKEN_PREFIX + refreshToken, userInfo.id(), 60 * 24 * 7);
        return UserLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String findRefreshTokenByUserId(Long userId) {
        // 패턴을 설정 (모든 refreshToken 키 탐색)
        String pattern = "refreshToken:*";
        Set<String> keys = redisUtil.scan(pattern);

        // 각 키에 대해 해당하는 값을 확인
        for (String key : keys) {
            Long storedUserId = Long.parseLong(String.valueOf(redisUtil.get(key)));
            if (storedUserId.equals(userId)) {
                return key; // refreshToken 반환
            }
        }
        return null; // 찾지 못한 경우
    }


}