package com.esquad.esquadbe.chat.controller;

import com.esquad.esquadbe.chat.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/file")
public class S3FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> fileUpload(@RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                        @RequestParam("username") String username) {

        return fileService.uploadFile(multipartFile, username);
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok("File deleted successfully with " + filename);
    }
}
