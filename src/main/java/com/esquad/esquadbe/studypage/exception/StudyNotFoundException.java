package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class StudyNotFoundException extends RestApiException {

    public StudyNotFoundException() {
        super(StudyPageErrorCode.STUDY_NOT_FOUND_EXCEPTION);
    }
}
