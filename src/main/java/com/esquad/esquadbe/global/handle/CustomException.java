package com.esquad.esquadbe.global.handle;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException implements Serializable {

   private final ErrorCode errorCode;

   public CustomException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
   }

}
