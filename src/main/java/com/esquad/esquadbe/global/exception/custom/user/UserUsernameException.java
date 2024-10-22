package com.esquad.esquadbe.global.exception.custom.user;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.UserErrorCode;

public class UserUsernameException extends RestApiException {
    public UserUsernameException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }
}
