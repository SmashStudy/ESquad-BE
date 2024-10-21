package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserUsernameException extends RestApiException {
    public UserUsernameException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }
}
