package com.esquad.esquadbe.storage.exception;

import com.esquad.esquadbe.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode implements ErrorCode {

   //   USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "User not exists", "USER-0001"),
   FILE_NOT_EXISTS_ERROR(HttpStatus.NOT_FOUND, "File not exists", "STORAGE-0001"),
   UPLOAD_FILE_EMPTY_ERROR(HttpStatus.NOT_FOUND, "File is empty", "STORAGE-0002"),
   FILE_UPLOAD_UNAVAILABLE(HttpStatus.NOT_FOUND, "File upload failed", "STORAGE-0003"),
   FILE_DOWNLOAD_UNAVAILABLE(HttpStatus.NOT_FOUND, "File download failed", "STORAGE-0004"),
   FILE_DELETE_UNAVAILABLE_ERROR(HttpStatus.NOT_FOUND, "File delete failed", "STORAGE-0005");


   private final HttpStatus httpStatus;
   private final String message;
   private final String code;

}
