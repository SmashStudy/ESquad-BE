package com.esquad.esquadbe.user.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String accessToken, String refreshToken) {
}
