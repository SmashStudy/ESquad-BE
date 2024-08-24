package com.esquad.esquadbe.storage.service;

import com.esquad.esquadbe.storage.entity.FileInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3FileService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileInfo uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        FileInfo fileInfo = FileInfo.of(multipartFile.getOriginalFilename(), multipartFile);

        try {
            final var putObjectRequest = getPutObjectRequest(multipartFile,
                fileInfo.getStoredFileName());
            final RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드에 실패하였습니다: " + e.getMessage());
        }

        return fileInfo;
    }

    public void deleteFile(String storedFileName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(storedFileName)
                .build());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("파일 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    public byte[] downloadFile(String storedFileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(storedFileName)
                .build();

            ResponseBytes<GetObjectResponse> s3Object = s3Client.getObjectAsBytes(getObjectRequest);
            return s3Object.asByteArray();

        } catch (RuntimeException e) {
            throw new IllegalArgumentException("파일 다운로드에 실패하였습니다: " + e.getMessage());
        }
    }

    private PutObjectRequest getPutObjectRequest(MultipartFile multipartFile,
        final String fileName) {
        return PutObjectRequest.builder()
            .bucket(bucket)
            .contentType(multipartFile.getContentType())
            .contentLength(multipartFile.getSize())
            .key(fileName)
            .build();
    }
}
