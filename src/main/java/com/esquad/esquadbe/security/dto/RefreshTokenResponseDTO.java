package com.esquad.esquadbe.security.dto;

import lombok.Builder;

@Builder
public record RefreshTokenResponseDTO(String accessToken, String refreshToken) {
}
