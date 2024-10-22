package com.esquad.esquadbe.storage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class FileUploadFailureException extends RestApiException {

    public FileUploadFailureException() {
        super(StorageErrorCode.FILE_UPLOAD_UNAVAILABLE);
    }
}
