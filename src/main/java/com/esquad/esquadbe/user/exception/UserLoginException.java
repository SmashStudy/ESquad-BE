package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserLoginException extends RestApiException {

    public UserLoginException() {
        super(UserErrorCode.USER_AUTHENTICATION_ERROR);
    }
}
