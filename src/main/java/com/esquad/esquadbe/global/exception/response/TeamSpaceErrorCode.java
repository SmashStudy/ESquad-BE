package com.esquad.esquadbe.global.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamSpaceErrorCode implements ErrorCode {

   TEAM_ALREADY_EXISTS_EXCEPTION(HttpStatus.CONFLICT, 409, "이미 존재하는 팀명입니다.", "TSE0001");

   private final HttpStatus httpStatus;
   private final int statusCode;
   private final String message;
   private final String code;

}
