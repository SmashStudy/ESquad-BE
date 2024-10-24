package com.esquad.esquadbe.studypage.exception;

import com.esquad.esquadbe.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StudyPageErrorCode implements ErrorCode {
   BOOK_JSON_PROCESSING_EXCEPTION(HttpStatus.UNAUTHORIZED,"An error occurred while processing the JSON response.", "STUDYPAGE-0005"),
   BOOK_URI_EXCEPTION(HttpStatus.BAD_REQUEST, "네이버 API 요청 실패","STUDYPAGE-0006"),
   STUDY_NAME_NOT_EQUAL_EXCEPTION(HttpStatus.BAD_REQUEST,"Not Equal Study Name. You can't Delete This Study Page","STUDYPAGE-0008"),
   BOOK_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"book not found","STUDYPAGE-0009"),
   STUDY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"book not found","STUDYPAGE-0010"),
//   (HttpStatus.,"","STUDYPAGE-0011"),
//   (HttpStatus.,"","STUDYPAGE-0012-"),
   ;

   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
