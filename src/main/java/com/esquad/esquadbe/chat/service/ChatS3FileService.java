package com.esquad.esquadbe.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.UUID;

@Service
public class ChatS3FileService {
    @Value("${cloud.aws.s3.chat_bucket}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString()+ "_" + file.getOriginalFilename();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String fileName) {
        try{
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName).build());
        } catch (RuntimeException e) {
            throw new RuntimeException("파일 삭제에 실패했음 : " + e.getMessage());
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            ResponseBytes<GetObjectResponse> s3FileObjet = s3Client.getObjectAsBytes(getObjectRequest);
            return s3FileObjet.asByteArray();
        }catch (RuntimeException e) {
            throw new IllegalArgumentException("파일 다운로드에 실패했음 : " + e.getMessage());
        }
    }
}
