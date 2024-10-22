package com.esquad.esquadbe.global.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

   USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "User not exists", "USER-0001"),
   USER_AUTHENTICATION_ERROR(HttpStatus.NOT_FOUND, "User doesn't exists", "USER-0002"),
   USER_REFRESH_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "User refreshToken not exists", "USER-0003"),
   USER_REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "User refreshToken invalid", "USER-0004");

   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
