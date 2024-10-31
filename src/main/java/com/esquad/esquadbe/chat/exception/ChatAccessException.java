package com.esquad.esquadbe.chat.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class ChatAccessException extends RestApiException {
    public ChatAccessException() {
        super(ChatErrorCode.CHAT_NOT_CORRECT_ID);
    }
}
