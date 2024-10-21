package com.esquad.esquadbe.global.exception.response;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}


