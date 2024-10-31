package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserNotFoundException extends RestApiException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }
}