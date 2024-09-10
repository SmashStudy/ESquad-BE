package com.esquad.esquadbe.security.jwt;


import com.esquad.esquadbe.redis.RedisUtil;

import com.esquad.esquadbe.security.dto.RefreshTokenResponseDTO;
import com.esquad.esquadbe.security.exception.RefreshTokenException;
import com.esquad.esquadbe.security.exception.RefreshTokenExceptionResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil; // Redis와 상호작용하는 유틸리티

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    /**
     * refresh token을 이용하여 access token, refresh token 재발급
     *
     * @param refreshToken refresh token
     * @return RefreshTokenResponseDTO
     */

    public RefreshTokenResponseDTO refreshToken(final String refreshToken) {
        // refresh token 유효성 검증
        checkRefreshToken(refreshToken);

        // Redis에서 refresh token으로 사용자 id 조회
        Long id = (Long) redisUtil.get(REFRESH_TOKEN_PREFIX + refreshToken);
        if (id == null) {
            throw new RefreshTokenException(RefreshTokenExceptionResult.NOT_EXIST);
        }

        // 새로운 access token 생성
        String newAccessToken = jwtProvider.generateAccessToken(id);

        // 기존에 가지고 있는 사용자의 refresh token 제거

        redisUtil.delete(REFRESH_TOKEN_PREFIX + refreshToken);

        // 새로운 refresh token 생성 후 저장 (Redis에 저장)
        String newRefreshToken = jwtProvider.generateRefreshToken(id);
        redisUtil.set(REFRESH_TOKEN_PREFIX + newRefreshToken, id, 60 * 24 * 7); // 7일간 유지

        return RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * refresh token 검증
     *
     * @param refreshToken refresh token
     */
    private void checkRefreshToken(final String refreshToken) {
        if (Boolean.FALSE.equals(jwtProvider.validateToken(refreshToken))) {
            throw new RefreshTokenException(RefreshTokenExceptionResult.INVALID);
        }
    }
}