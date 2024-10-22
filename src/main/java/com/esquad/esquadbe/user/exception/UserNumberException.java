package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserNumberException extends RestApiException {
    public UserNumberException() {
        super(UserErrorCode.USER_NUMBER_MISMATCH);
    }
}
