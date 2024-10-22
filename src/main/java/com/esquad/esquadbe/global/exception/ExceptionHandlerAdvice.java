package com.esquad.esquadbe.global.exception;

import com.esquad.esquadbe.chat.exception.ChatAccessException;
import com.esquad.esquadbe.chat.exception.ChatException;
import com.esquad.esquadbe.storage.exception.FileDeleteFailureException;
import com.esquad.esquadbe.storage.exception.FileDownloadFailureException;
import com.esquad.esquadbe.storage.exception.FileIsEmptyException;
import com.esquad.esquadbe.storage.exception.FileNotExistsException;
import com.esquad.esquadbe.storage.exception.FileUploadFailureException;
import com.esquad.esquadbe.team.exception.DuplicateTeamException;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.team.exception.TeamUserRolePermissionException;
import com.esquad.esquadbe.user.exception.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<?> handleChatException(ChatException e) {
        log.error("[ChatException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(ChatAccessException.class)
    public ResponseEntity<?> handleChatAccessException(ChatAccessException e) {
        log.error("[ChatAccessException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    // 사용자 정의 예외
    @ExceptionHandler(UserInquiryException.class)
    public ResponseEntity<?> handleUserInquiryException(UserInquiryException e) {
        log.error("[UserInquiryException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<?> handleUserLoginException(UserLoginException e) {
        log.error("[UserLoginException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(UserUsernameException.class)
    public ResponseEntity<?> handleUserUsernameException(UserUsernameException e) {
        log.error("[UserUsernameException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(UserNicknameException.class)
    public ResponseEntity<?> handleUserNicknameException(UserNicknameException e) {
        log.error("[UserNicknameException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        log.error("[UserNotFoundException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(UserRefreshTokenException.class)
    public ResponseEntity<?> handleUserRefreshTokenException(UserRefreshTokenException e) {
        log.error("[UserRefreshTokenException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(DuplicateTeamException.class)
    public ResponseEntity<?> handleDuplicateTeamException(DuplicateTeamException e) {
        log.error("[DuplicateTeamException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<?> handleTeamNotFoundException(TeamNotFoundException e) {
        log.error("[TeamNotFoundException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(TeamUserRolePermissionException.class)
    public ResponseEntity<?> handleTeamUserRolePermissionException(TeamUserRolePermissionException e) {
        log.error("[TeamUserRolePermissionException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(FileDeleteFailureException.class)
    public ResponseEntity<?> handleFileDeleteFailureException(FileDeleteFailureException e) {
        log.error("[FileDeleteFailureException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(FileDownloadFailureException.class)
    public ResponseEntity<?> handleFileDownloadFailureException(FileDownloadFailureException e) {
        log.error("[FileDownloadFailureException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<?> handleFileIsEmptyException(FileIsEmptyException e) {
        log.error("[FileIsEmptyException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(FileNotExistsException.class)
    public ResponseEntity<?> handleFileException(FileNotExistsException e) {
        log.error("[FileNotExistsException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(FileUploadFailureException.class)
    public ResponseEntity<?> handleFileException(FileUploadFailureException e) {
        log.error("[FileUploadFailureException] message: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<?> handleSystemException(RestApiException e) {
        log.error("[RestApiException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return handelInternalException(errorCode);
    }


    // 자주 발생하는 예외 사항
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        log.error("[NullPointerException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.NULL_POINTER_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    // 메서드가 잘못되었거나 부적합한 인수를 전달한 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[IllegalArgumentException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.ILLEGAL_ARGUMENT_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("[EntityNotFoundException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.ENTITY_NOT_FOUND_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("[DataIntegrityViolationException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.DATA_INTEGRITY_VIOLATION_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException e) {
        log.error("[TransactionSystemException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.TRANSACTION_SYSTEM_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[HttpRequestMethodNotSupportedException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.METHOD_NOT_SUPPORTED_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("[ConstraintViolationException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.CONSTRAINT_VIOLATION_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        log.error("[NoSuchElementException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.NO_SUCH_ELEMENT_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    // @Valid 유효성 검사에서 예외가 발생했을 때 (requestbody에 잘못 들어온 경우)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.METHOD_ARGUMENT_NOT_VALID_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("[MissingServletRequestParameterException] message: {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.MISSING_REQUEST_PARAMETER_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    // Exception 예외 사항
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handelInternalException(errorCode, NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    private ResponseEntity<?> handelInternalException(final ErrorCode errorCode) {
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    private ResponseEntity<?> handelInternalException(final ErrorCode errorCode, String message) {
        final ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                String.format("%s %s", errorCode.getMessage(), message)
        );
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
