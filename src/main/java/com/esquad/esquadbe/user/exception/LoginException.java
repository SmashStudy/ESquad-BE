package com.esquad.esquadbe.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginException extends RuntimeException {

    private final LoginExceptionResult loginErrorResult;

}
