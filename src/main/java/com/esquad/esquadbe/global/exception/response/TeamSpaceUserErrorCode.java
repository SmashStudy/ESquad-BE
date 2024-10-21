package com.esquad.esquadbe.global.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamSpaceUserErrorCode implements ErrorCode {

   TEAM_USER_ROLE_PERMIT_EXCEPTION(HttpStatus.UNAUTHORIZED, "Improper authority", "TEAMUSER-0001");

   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
