package com.esquad.esquadbe.global.exception.custom;

import com.esquad.esquadbe.global.exception.response.CommonErrorCode;
import com.esquad.esquadbe.global.exception.response.ErrorCode;

public class DuplicateException extends BusinessBaseException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
