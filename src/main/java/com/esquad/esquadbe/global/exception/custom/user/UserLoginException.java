package com.esquad.esquadbe.global.exception.custom.user;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.UserErrorCode;

public class UserLoginException extends RestApiException {

    public UserLoginException() {
        super(UserErrorCode.USER_AUTHENTICATION_ERROR);
    }

}
