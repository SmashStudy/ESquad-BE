package com.esquad.esquadbe.qnaboard.exception;

import com.esquad.esquadbe.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QnaErrorCode implements ErrorCode {

    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Question doesn't exist", "QNA-001"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment doesn't exist", "COMMENT-001"),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "Book doesn't exist", "BOOK-001");


    private final HttpStatus httpStatus;
    private final String message;
    private final String code;


}
