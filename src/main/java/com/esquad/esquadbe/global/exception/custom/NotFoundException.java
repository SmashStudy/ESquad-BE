package com.esquad.esquadbe.global.exception.custom;

import com.esquad.esquadbe.global.exception.response.CommonErrorCode;
import com.esquad.esquadbe.global.exception.response.ErrorCode;

public class NotFoundException extends BusinessBaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
