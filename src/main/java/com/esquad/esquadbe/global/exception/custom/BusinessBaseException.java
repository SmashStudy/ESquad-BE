package com.esquad.esquadbe.global.exception.custom;

import com.esquad.esquadbe.global.exception.response.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessBaseException extends RuntimeException {
   private final ErrorCode errorCode;

   public BusinessBaseException(String message, ErrorCode errorCode) {
      super(message);
      this.errorCode = errorCode;
   }
}
