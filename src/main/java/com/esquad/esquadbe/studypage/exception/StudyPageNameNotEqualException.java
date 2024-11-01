package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class StudyPageNameNotEqualException extends RestApiException {

    public StudyPageNameNotEqualException () {
        super(StudyPageErrorCode.STUDY_NAME_NOT_EQUAL_EXCEPTION);
    }
}
