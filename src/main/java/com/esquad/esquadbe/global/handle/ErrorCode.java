package com.esquad.esquadbe.global.handle;

import lombok.Getter;

@Getter
public enum ErrorCode {

   DUPLICATE_NAME(403, "804", "중복된 이름은 사용할 수 없습니다.");

   private final int status;
   private final String code;
   private final String message;

   private ErrorCode(final int status, final String code, final String message) {
      this.status = status;
      this.code = code;
      this.message = message;
   }
}
