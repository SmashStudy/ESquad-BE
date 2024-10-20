package com.esquad.esquadbe.global.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

   USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, 404, "존재하지 않는 유저입니다.", "UE0001");

   private final HttpStatus httpStatus;
   private final int statusCode;
   private final String message;
   private final String code;

}
