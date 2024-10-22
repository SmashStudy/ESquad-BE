package com.esquad.esquadbe.chat.exception;

import com.esquad.esquadbe.global.exception.RestApiException;


public class ChatFileException extends RestApiException {
    public ChatFileException() {
        super(ChatErrorCode.CHAT_FILE_NOT_FOUND);
    }
}
