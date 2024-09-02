package com.esquad.esquadbe.global.handle;

import java.io.Serializable;

public class DuplicateNameException extends CustomException implements Serializable {

   public DuplicateNameException(ErrorCode errorCode) {
      super(errorCode);
   }

}
