package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class BookJsonProcessingException extends RestApiException  {

    public BookJsonProcessingException() {
        super(StudyPageErrorCode.BOOK_JSON_PROCESSING_EXCEPTION);
    }
}