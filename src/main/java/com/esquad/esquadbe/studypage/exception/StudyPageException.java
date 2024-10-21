package com.esquad.esquadbe.studypage.exception;

public class StudyPageException extends RuntimeException {
    public StudyPageException(String message) {
        super(message);
    }

    public StudyPageException(String message, Throwable cause) {
        super(message, cause);
    }
}
