package com.esquad.esquadbe.chat.service;

import com.esquad.esquadbe.chat.dto.ChatS3FileDto;
import com.esquad.esquadbe.chat.entity.ChatS3FileEntity;
import com.esquad.esquadbe.chat.repository.ChatS3FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ChatS3FileService s3FileService;
    private final ChatS3FileRepository s3FileRepository;

    @Transactional
    public ResponseEntity<?> uploadFile(MultipartFile file, String username) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
            }
            String storedFileName = s3FileService.uploadFile(file);

            ChatS3FileDto s3FileDto = new ChatS3FileDto(username, file.getOriginalFilename(), storedFileName, LocalDate.now());

            ChatS3FileEntity fileEntity = new ChatS3FileEntity(s3FileDto.getUserName(), s3FileDto.getOriginalFileName(), s3FileDto.getFileName());

            s3FileRepository.save(fileEntity);
            return ResponseEntity.ok(s3FileDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public void deleteFile (String filename) {
        ChatS3FileEntity s3FileEntity = s3FileRepository.findByFileName(filename)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 해당 파일이 없음" + filename));
        s3FileRepository.delete(s3FileEntity);
    }

    public ResponseEntity<byte[]> downloadFile(String fileName) {
        ChatS3FileEntity s3FileEntity = s3FileRepository.findByFileName(fileName)
                .orElseThrow(() -> new RuntimeException("다운로드 할 파일이 없음 : " + fileName));

        byte[] fileData = s3FileService.downloadFile(s3FileEntity.getFileName());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + s3FileEntity.getOriginalFilename());
        headers.setContentLength(fileData.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }
}