package com.esquad.esquadbe.user.exception;


import com.esquad.esquadbe.global.exception.RestApiException;

public class UserInquiryException extends RestApiException {

    public UserInquiryException() {
        super(UserErrorCode.USER_NOT_FOUND_ERROR);
    }

}