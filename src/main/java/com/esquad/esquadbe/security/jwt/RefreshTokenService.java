package com.esquad.esquadbe.security.jwt;


import com.esquad.esquadbe.redis.RedisUtil;

import com.esquad.esquadbe.security.dto.RefreshTokenResponseDTO;


import com.esquad.esquadbe.user.exception.UserRefreshTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    public RefreshTokenResponseDTO refreshToken(final String refreshToken) {
        checkRefreshToken(refreshToken);

        Long id = (Long) redisUtil.get(REFRESH_TOKEN_PREFIX + refreshToken);
        if (id == null) {
            throw new UserRefreshTokenException();
        }

        String newAccessToken = jwtProvider.generateAccessToken(id);

        redisUtil.delete(REFRESH_TOKEN_PREFIX + refreshToken);

        String newRefreshToken = jwtProvider.generateRefreshToken(id);
        redisUtil.set(REFRESH_TOKEN_PREFIX + newRefreshToken, id, 60 * 24 * 7);

        return RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private void checkRefreshToken(final String refreshToken) {
        if (Boolean.FALSE.equals(jwtProvider.validateToken(refreshToken))) {
            throw new UserRefreshTokenException();
        }
    }
}