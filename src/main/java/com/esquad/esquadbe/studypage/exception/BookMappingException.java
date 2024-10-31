package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class BookMappingException extends RestApiException{

    public BookMappingException () {
        super(StudyPageErrorCode.BOOK_JSON_PROCESSING_EXCEPTION);
    }
}
