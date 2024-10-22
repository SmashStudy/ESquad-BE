package com.esquad.esquadbe.storage.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class FileDownloadFailureException extends RestApiException {
    public FileDownloadFailureException() {
        super(StorageErrorCode.FILE_DOWNLOAD_UNAVAILABLE);
    }


}
