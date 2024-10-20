package com.esquad.esquadbe.global.exception.handler;

import com.esquad.esquadbe.global.exception.response.CommonErrorCode;
import com.esquad.esquadbe.global.exception.response.ErrorCode;
import com.esquad.esquadbe.global.exception.response.ErrorResponse;
import com.esquad.esquadbe.global.exception.custom.BusinessBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return of(errorCode);
    }

    @ExceptionHandler(BusinessBaseException.class)
    public ResponseEntity<Object> handleSystemException(BusinessBaseException e) {
        log.error("[SystemException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return of(errorCode);
    }

    // 메서드가 잘못되었거나 부적합한 인수를 전달한 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[handleIllegalArgumentException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        ErrorCode errorCode = CommonErrorCode.ILLEGAL_ARGUMENT_ERROR;
        return of(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    private ResponseEntity<Object> of(final ErrorCode errorCode) {
        final ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.getHttpStatus(),
                errorCode.getStatusCode(),
                errorCode.getMessage()
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    private ResponseEntity<Object> of(final ErrorCode errorCode, String message) {
        final ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.getHttpStatus(),
                errorCode.getStatusCode(),
                String.format("%s %s", errorCode.getMessage(), message)
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

}
