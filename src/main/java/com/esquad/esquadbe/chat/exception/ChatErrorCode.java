package com.esquad.esquadbe.chat.exception;

import com.esquad.esquadbe.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 채팅 예외 클래스
@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode{

    CHAT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Chat File not exists", "CHAT-0001"),
    CHAT_NOT_CORRECT_ID(HttpStatus.BAD_REQUEST, "Chat not correct id", "CHAT-0002");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;
}
