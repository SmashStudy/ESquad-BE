package com.esquad.esquadbe.storage.controller;

import com.esquad.esquadbe.storage.entity.FileInfo;
import com.esquad.esquadbe.storage.service.S3FileService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3FileController {


    private final S3FileService s3FileService;

    @PostMapping
    public ResponseEntity<FileInfo> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(s3FileService.uploadFile(file));
    }

    @DeleteMapping("/{storedFileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String storedFileName) {
        s3FileService.deleteFile(storedFileName);
        return ResponseEntity.status(HttpStatus.OK)
            .body("파일명 : " + storedFileName + " 정상적으로 삭제되었습니다.");
    }

    @GetMapping("/{storedFileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String storedFileName) {
        try {
            byte[] data = s3FileService.downloadFile(storedFileName);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(
                    storedFileName, StandardCharsets.UTF_8))
                .contentLength(data.length)
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}