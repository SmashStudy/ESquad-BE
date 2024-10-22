package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserPasswordException extends RestApiException {
    public UserPasswordException() {
        super(UserErrorCode.USER_PASSWORD_NOT_MATCH);
    }
}
