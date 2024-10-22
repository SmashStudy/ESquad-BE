package com.esquad.esquadbe.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

   INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "COMMON-0001"),
   INVALID_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "Invalid parameter included", "COMMON-0002"),
   INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "Invalid argument", "COMMON-0003"),
   ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "Illegal argument", "COMMON-0004"),
   NULL_POINTER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Null pointer exception occurred", "COMMON-0005"),
   ENTITY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Entity not found", "COMMON-0006"),
   DATA_INTEGRITY_VIOLATION_ERROR(HttpStatus.CONFLICT, "Data integrity violation", "COMMON-0007"),
   TRANSACTION_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction system error", "COMMON-0008"),
   METHOD_NOT_SUPPORTED_ERROR(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not supported", "COMMON-0009"),
   CONSTRAINT_VIOLATION_ERROR(HttpStatus.BAD_REQUEST, "Constraint violation", "COMMON-0010"),
   NO_SUCH_ELEMENT_ERROR(HttpStatus.NOT_FOUND, "No such element found", "COMMON-0011"),
   METHOD_ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "Method argument not valid", "COMMON-0012"),
   MISSING_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "Missing request parameter", "COMMON-0013")
   ;

   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
