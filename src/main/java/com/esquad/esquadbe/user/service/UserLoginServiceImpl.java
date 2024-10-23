package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.redis.RedisUtil;
import com.esquad.esquadbe.user.dto.UserLoginRequestDTO;
import com.esquad.esquadbe.user.dto.UserLoginResponseDTO;

import com.esquad.esquadbe.user.dto.UserGetResponseDTO;



import com.esquad.esquadbe.security.jwt.JwtProvider;

import com.esquad.esquadbe.user.exception.UserLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserInquiryServiceImpl userGetService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtProvider jwtProvider;

    private final RedisUtil redisUtil;

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    @Override
    @Transactional
    public UserLoginResponseDTO login(final UserLoginRequestDTO loginRequestDTO) {
        UserGetResponseDTO userInfo = userGetService.getUsername(loginRequestDTO.getUsername());

        if(!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), userInfo.password()))
            throw new UserLoginException();

        String accessToken = jwtProvider.generateAccessToken(userInfo.id());

        String existingRefreshToken = findRefreshTokenByUserId(userInfo.id());
        if (existingRefreshToken != null) {
            redisUtil.delete(existingRefreshToken);
        }


        String refreshToken = jwtProvider.generateRefreshToken(userInfo.id());
        redisUtil.set(REFRESH_TOKEN_PREFIX + refreshToken, userInfo.id(), 60 * 24 * 7);
        return UserLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String findRefreshTokenByUserId(Long userId) {
        String pattern = "refreshToken:*";
        Set<String> keys = redisUtil.scan(pattern);

        for (String key : keys) {
            Long storedUserId = Long.parseLong(String.valueOf(redisUtil.get(key)));
            if (storedUserId.equals(userId)) {
                return key;
            }
        }
        return null;
    }


}