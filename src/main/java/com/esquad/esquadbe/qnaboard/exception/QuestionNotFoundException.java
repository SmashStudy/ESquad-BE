package com.esquad.esquadbe.qnaboard.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class QuestionNotFoundException extends RestApiException {

    public QuestionNotFoundException() {
        super(QnaErrorCode.QUESTION_NOT_FOUND);
    }
}
