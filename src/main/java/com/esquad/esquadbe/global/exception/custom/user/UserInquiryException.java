package com.esquad.esquadbe.global.exception.custom.user;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.UserErrorCode;

public class UserInquiryException extends RestApiException {

    public UserInquiryException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }

}