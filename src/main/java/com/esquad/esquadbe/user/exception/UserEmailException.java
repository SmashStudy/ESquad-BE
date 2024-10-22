package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserEmailException extends RestApiException {
    public UserEmailException() {
        super(UserErrorCode.USER_EMAIL_NOT_EXIST);
    }
}
