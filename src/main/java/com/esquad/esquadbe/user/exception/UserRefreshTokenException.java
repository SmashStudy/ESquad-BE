package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

/**
 * Refresh Token Exception
 */
public class UserRefreshTokenException extends RestApiException {

    public UserRefreshTokenException() {
        super(UserErrorCode.USER_REFRESH_TOKEN_NOT_EXIST);
    }

}
