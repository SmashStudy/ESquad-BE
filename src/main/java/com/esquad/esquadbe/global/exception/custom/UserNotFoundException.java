package com.esquad.esquadbe.global.exception.custom;

import com.esquad.esquadbe.global.exception.response.UserErrorCode;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }
}
