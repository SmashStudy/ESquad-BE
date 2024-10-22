package com.esquad.esquadbe.team.exception;

import com.esquad.esquadbe.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamSpaceErrorCode implements ErrorCode {

   TEAM_ALREADY_EXISTS_EXCEPTION(HttpStatus.CONFLICT, "Duplicate team name", "TEAM-0001"),
   TEAM_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "Team doesn't exists", "TEAM-0002");

   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
