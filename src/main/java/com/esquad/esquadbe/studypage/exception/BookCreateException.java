package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class BookCreateException extends RestApiException{

    public BookCreateException() {
        super(StudyPageErrorCode.BOOK_CREATE_EXCEPTION);
    }
}
