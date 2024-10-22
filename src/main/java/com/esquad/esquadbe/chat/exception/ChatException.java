package com.esquad.esquadbe.chat.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class ChatException extends RestApiException {
    public ChatException() {
        super(ChatErrorCode.CHAT_NOT_FOUND);
    }
}
