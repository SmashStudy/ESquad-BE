package com.esquad.esquadbe.chat.service;

import com.esquad.esquadbe.chat.dto.ChatS3FileDto;
import com.esquad.esquadbe.chat.entity.ChatS3FileEntity;
import com.esquad.esquadbe.chat.repository.ChatS3FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatFileService {
    @Value("${cloud.aws.s3.chat_bucket}")
    private String bucketName;

    private final ChatS3FileService s3FileService;
    private final ChatS3FileRepository s3FileRepository;

    @Transactional
    public ResponseEntity<?> uploadFile(MultipartFile file, String username) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
            }
            String storedFileName = s3FileService.uploadFile(file);
            String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + storedFileName; // S3 URL 설정

            ChatS3FileDto s3FileDto = new ChatS3FileDto(
                    username,
                    file.getOriginalFilename(),
                    storedFileName,
                    fileUrl, // fileUrl 추가
                    LocalDate.now()
            );

            ChatS3FileEntity fileEntity = new ChatS3FileEntity(
                    s3FileDto.getUserName(),
                    s3FileDto.getOriginalFileName(),
                    s3FileDto.getFileName(),
                    s3FileDto.getFileUrl() // fileUrl 추가
            );

            s3FileRepository.save(fileEntity);
            return ResponseEntity.ok(s3FileDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}