package com.esquad.esquadbe.storage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class FileNotExistsException extends RestApiException {
    public FileNotExistsException() {
        super(StorageErrorCode.FILE_NOT_EXISTS_ERROR);
    }

}
