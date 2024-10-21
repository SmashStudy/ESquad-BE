package com.esquad.esquadbe.global.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

   USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "User not exists", "USER-0001");

   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
