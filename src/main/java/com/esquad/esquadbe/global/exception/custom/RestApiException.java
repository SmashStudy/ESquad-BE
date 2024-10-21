package com.esquad.esquadbe.global.exception.custom;

import com.esquad.esquadbe.global.exception.response.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
   private final ErrorCode errorCode;

   public RestApiException(String message, ErrorCode errorCode) {
      super(message);
      this.errorCode = errorCode;
   }

   public RestApiException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
   }

}
