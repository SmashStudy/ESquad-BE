package com.esquad.esquadbe.global.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

   INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", "E50001"),

   INVALID_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, 400, "올바르지 않는 파라미터입니다.", "E40001"),
   INVALID_TYPE_ERROR(HttpStatus.BAD_REQUEST, 400, "올바르지 않는 타입입니다.", "E40002"),
   ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, 400, "필수 파라미터가 없습니다.", "E40003"),
   NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, 404, "대상이 존재하지 않습니다.", "E40004"),
   DUPLICATE_ERROR(HttpStatus.CONFLICT, 409, "대상이 이미 존재합니다.", "E40005" );

   private final HttpStatus httpStatus;
   private final int statusCode;
   private final String message;
   private final String code;

}
