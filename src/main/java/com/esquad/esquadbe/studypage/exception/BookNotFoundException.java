package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class BookNotFoundException extends RestApiException {

    public BookNotFoundException() {
        super(StudyPageErrorCode.BOOK_NOT_FOUND_EXCEPTION);
    }
}
