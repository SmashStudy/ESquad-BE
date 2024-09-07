package com.esquad.esquadbe.storage.service;

import static org.junit.jupiter.api.Assertions.*;

import com.esquad.esquadbe.storage.dto.ResponseFileDto;
import com.esquad.esquadbe.storage.entity.TargetType;
import com.esquad.esquadbe.storage.repository.StoredFileRepository;
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

    @Autowired
    StoredFileRepository storedFileRepository;

    @Test
    @DisplayName("MultipartFile 업로드 시 예외가 발생하지 않는다")
    void testUploadFile() {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());

        final Long targetId = 1L;
        final TargetType targetType = TargetType.QNA;

        // Then
        assertDoesNotThrow(() -> s3FileService.uploadFile(mockFile, targetId, targetType, 1L));
    }

    @Test
    @DisplayName("MultipartFile 다운로드 시 예외가 발생하지 않는다")
    void testDownloadFile() {
        // Given
        final String fileName = "filename.txt";
        final String fileContent = "test content";
        final MultipartFile mockFile = new MockMultipartFile("file", fileName, "text/plain",
            fileContent.getBytes());

        final Long targetId = 1L;
        final TargetType targetType = TargetType.QNA;

        ResponseFileDto uploadedFileInfo = s3FileService.uploadFile(mockFile, targetId, targetType,
            1L);
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

        final Long targetId = 1L;
        final TargetType targetType = TargetType.QNA;

        final String fileKey = s3FileService.uploadFile(mockFile, targetId, targetType, 1L)
            .getStoredFileName();

        // Then
        assertDoesNotThrow(() -> s3FileService.deleteFile(fileKey));
    }

    @Test
    @DisplayName("파일 업로드 후 저장된 엔티티가 존재하는지 검증")
    void testUploadFileAndStoredInDB() {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());
        final Long targetId = 1L;
        final TargetType targetType = TargetType.QNA;

        // When
        ResponseFileDto fileInfo = s3FileService.uploadFile(mockFile, targetId, targetType, 1L);

        // Then
        assertTrue(
            storedFileRepository.existsByFileInfo_StoredFileName(fileInfo.getStoredFileName()));
    }

    @Test
    @DisplayName("파일 삭제 후 엔티티가 삭제되었는지 검증")
    void testDeleteFileAndRemoveFromDB() {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());
        final Long targetId = 1L;
        final TargetType targetType = TargetType.QNA;
        final String fileKey = s3FileService.uploadFile(mockFile, targetId, targetType, 1L)
            .getStoredFileName();

        // When
        s3FileService.deleteFile(fileKey);

        // Then
        assertFalse(storedFileRepository.existsByFileInfo_StoredFileName(fileKey));
    }

    @Test
    @DisplayName("존재하지 않는 파일 다운로드 시 예외 발생")
    void testDownloadNonExistentFile() {
        // Given
        final String nonExistentFileName = "non-existent-file.txt";

        // Then
        assertThrows(IllegalArgumentException.class,
            () -> s3FileService.downloadFile(nonExistentFileName));
    }

    @Test
    @DisplayName("존재하지 않는 파일 삭제 시 예외 발생")
    void testDeleteNonExistentFile() {
        // Given
        final String nonExistentFileName = "non-existent-file.txt";

        // Then
        assertThrows(IllegalArgumentException.class,
            () -> s3FileService.deleteFile(nonExistentFileName));
    }
}
