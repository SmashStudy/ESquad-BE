package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserPasswordPatternException extends RestApiException {
    public UserPasswordPatternException() {
        super(UserErrorCode.USER_PASSWORD_PATTERN_ERROR);
    }
}
