package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class BookUriException extends RestApiException {

    public BookUriException() {
        super(StudyPageErrorCode.BOOK_JSON_PROCESSING_EXCEPTION);
    }
}
