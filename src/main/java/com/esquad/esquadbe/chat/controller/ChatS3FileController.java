package com.esquad.esquadbe.chat.controller;

import com.esquad.esquadbe.chat.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/file")
public class ChatS3FileController {

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

    @GetMapping ("/download/{filename}")
    public ResponseEntity<ByteArrayResource> fileDownload(@PathVariable String filename) {
        try {
            ResponseEntity<byte[]> data = fileService.downloadFile(filename);

            ByteArrayResource resource = new ByteArrayResource(data.getBody());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename = " + URLEncoder.encode(
                                    filename, StandardCharsets.UTF_8))
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
