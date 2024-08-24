package com.esquad.esquadbe.storage.service;

import static org.junit.jupiter.api.Assertions.*;

import com.esquad.esquadbe.storage.entity.FileInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class S3FileServiceTest {

    @Autowired
    S3FileService s3FileService;
    @Test
    @DisplayName("MultipartFile 업로드 시 예외가 발생하지 않는다")
    void testUploadFile() {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());

        // Then
        assertDoesNotThrow(() -> s3FileService.uploadFile(mockFile));
    }
    @Test
    @DisplayName("MultipartFile 다운로드 시 예외가 발생하지 않는다")
    void testDownloadFile() {
        // Given
        final String fileName = "filename.txt";
        final String fileContent = "test content";
        final MultipartFile mockFile = new MockMultipartFile("file", fileName, "text/plain", fileContent.getBytes());

        FileInfo uploadedFileInfo = s3FileService.uploadFile(mockFile);

        String storedFileName = uploadedFileInfo.getStoredFileName();

        // When
        byte[] downloadedContent = s3FileService.downloadFile(storedFileName);

        // Then
        assertDoesNotThrow(() -> s3FileService.downloadFile(storedFileName));
        assertNotNull(downloadedContent);
    }



    @Test
    @DisplayName("MultipartFile 삭제 요청시 예외가 발생하지 않는다")
    void testDeleteFile() {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());
        final String fileKey = s3FileService.uploadFile(mockFile).getStoredFileName();

        // Then
        assertDoesNotThrow(() -> s3FileService.deleteFile(fileKey));
    }
}
