package com.esquad.esquadbe.storage.service;

import com.esquad.esquadbe.storage.dto.ResponseFileDto;
import com.esquad.esquadbe.storage.entity.FileInfo;
import com.esquad.esquadbe.storage.entity.StoredFile;
import com.esquad.esquadbe.storage.entity.TargetType;
import com.esquad.esquadbe.storage.repository.StoredFileRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final StoredFileRepository storedFileRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public ResponseFileDto uploadFile(MultipartFile multipartFile, Long targetId, TargetType targetType,
        Long userId) {
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
        User user = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        StoredFile storedFile = StoredFile.builder()
            .fileInfo(fileInfo)
            .targetId(targetId)
            .targetType(targetType)
            .user(user)
            .build();

        storedFileRepository.save(storedFile);

        return ResponseFileDto.from(storedFile);
    }

    @Transactional
    public void deleteFile(String storedFileName) {
        StoredFile storedFile = storedFileRepository.findByFileInfo_StoredFileName(storedFileName)
            .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다: " + storedFileName));

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(storedFileName)
                .build());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("파일 삭제에 실패했습니다: " + e.getMessage());
        }

        storedFileRepository.delete(storedFile);
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

    public List<ResponseFileDto> getFileList(Long targetId, TargetType targetType) {
        return storedFileRepository.findAllByTargetIdAndTargetType(targetId, targetType).stream()
            .map(ResponseFileDto::from).toList();
    }
}
