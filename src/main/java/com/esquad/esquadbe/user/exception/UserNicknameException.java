package com.esquad.esquadbe.user.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class UserNicknameException extends RestApiException {
    public UserNicknameException() { super(UserErrorCode.USER_NOT_FOUND_ERROR);}
}
