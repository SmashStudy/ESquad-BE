package com.esquad.esquadbe.storage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class FileDeleteFailureException extends RestApiException {
    public FileDeleteFailureException() {
        super(StorageErrorCode.FILE_DELETE_UNAVAILABLE_ERROR);
    }

}
