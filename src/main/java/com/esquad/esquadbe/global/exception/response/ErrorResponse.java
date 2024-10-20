package com.esquad.esquadbe.global.exception.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final boolean success = false;
    private final HttpStatus status;
    private final int code;
    private final String message;

    public static ErrorResponse of(HttpStatus status, int code, String message) {
        return ErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

}
