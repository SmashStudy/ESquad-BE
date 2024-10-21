package com.esquad.esquadbe.global.exception.custom.user;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.TeamSpaceErrorCode;
import com.esquad.esquadbe.global.exception.response.UserErrorCode;

public class UserNotFoundException extends RestApiException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }
}
