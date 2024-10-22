package com.esquad.esquadbe.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ChatFileServiceTest {

    @Autowired
    private ChatFileService chatFileService;

    @Autowired
    private ChatS3FileRepository s3FileRepository;

    @Autowired
    private ChatS3FileService s3FileService;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile(
                "file",
                "testfile.txt",
                "text/plain",
                "This is a test file".getBytes()
        );
    }

    @Test
    void testUploadFile() {
        String username = "testUser";

        // 파일 업로드
        ResponseEntity<?> responseEntity = chatFileService.uploadFile(mockFile, username);

        // 업로드 결과 확인
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isInstanceOf(ChatS3FileDto.class);

        // 저장된 파일 정보 확인
        ChatS3FileDto fileDto = (ChatS3FileDto) responseEntity.getBody();
        Optional<ChatS3FileEntity> savedFile = s3FileRepository.findByFileName(fileDto.getFileName());
        assertThat(savedFile).isPresent();
        assertThat(savedFile.get().getUsername()).isEqualTo(username);
    }

    @Test
    void testDeleteFile() {
        // 파일 업로드
        String username = "testUser";
        ResponseEntity<?> uploadResponse = chatFileService.uploadFile(mockFile, username);
        ChatS3FileDto fileDto = (ChatS3FileDto) uploadResponse.getBody();

        // 파일 삭제
        chatFileService.deleteFile(fileDto.getFileName());

        // 삭제 확인
        Optional<ChatS3FileEntity> deletedFile = s3FileRepository.findByFileName(fileDto.getFileName());
        assertThat(deletedFile).isEmpty();
    }

    @Test
    void testDownloadFile() {
        // 파일 업로드 후 다운로드 테스트
        String username = "testUser";
        ResponseEntity<?> uploadResponse = chatFileService.uploadFile(mockFile, username);
        ChatS3FileDto fileDto = (ChatS3FileDto) uploadResponse.getBody();

        // 파일 다운로드
        ResponseEntity<byte[]> downloadResponse = chatFileService.downloadFile(fileDto.getFileName());

        // 다운로드 결과 확인
        assertThat(downloadResponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(downloadResponse.getBody()).isNotNull();
        assertThat(downloadResponse.getHeaders().getContentDisposition().getFilename())
                .isEqualTo(fileDto.getOriginalFileName());
    }

    @Test
    void testDeleteNonExistentFileThrowsException() {
        String nonExistentFile = "non-existent-file.txt";

        // 존재하지 않는 파일 삭제 시도 -> 예외 발생 확인
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                chatFileService.deleteFile(nonExistentFile)
        );
        assertThat(exception.getMessage()).contains("삭제할 해당 파일이 없음");
    }

    @Test
    void testDownloadNonExistentFileThrowsException() {
        String nonExistentFile = "non-existent-file.txt";

        // 존재하지 않는 파일 다운로드 시도 -> 예외 발생 확인
        Exception exception = assertThrows(RuntimeException.class, () ->
                chatFileService.downloadFile(nonExistentFile)
        );
        assertThat(exception.getMessage()).contains("다운로드 할 파일이 없음");
    }
}
