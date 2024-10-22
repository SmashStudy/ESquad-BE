package com.esquad.esquadbe.global.exception.custom.user;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.UserErrorCode;

/**
 * Refresh Token Exception
 */
public class UserRefreshTokenException extends RestApiException {

    public UserRefreshTokenException() {
        super(UserErrorCode.USER_REFRESH_TOKEN_NOT_EXIST);
    }

}
