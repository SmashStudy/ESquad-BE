package com.esquad.esquadbe.qnaboard.exception;


import com.esquad.esquadbe.global.exception.RestApiException;

public class CommentNotFoundException extends RestApiException {
    public CommentNotFoundException() {
        super(QnaErrorCode.BOOK_NOT_FOUND);
    }
}
