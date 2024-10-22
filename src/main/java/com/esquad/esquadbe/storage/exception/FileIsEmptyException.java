package com.esquad.esquadbe.storage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class FileIsEmptyException extends RestApiException {
    public FileIsEmptyException() {
        super(StorageErrorCode.UPLOAD_FILE_EMPTY_ERROR);

    }
}
