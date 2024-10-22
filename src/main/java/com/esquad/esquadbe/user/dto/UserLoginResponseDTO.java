package com.esquad.esquadbe.user.dto;

import lombok.Builder;

@Builder
public record UserLoginResponseDTO(String accessToken, String refreshToken) {
}
