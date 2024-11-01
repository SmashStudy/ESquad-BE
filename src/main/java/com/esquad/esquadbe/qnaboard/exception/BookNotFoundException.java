package com.esquad.esquadbe.qnaboard.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class BookNotFoundException extends RestApiException{

    public BookNotFoundException() {
        super(QnaErrorCode.BOOK_NOT_FOUND);
    }
}
