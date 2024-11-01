package com.esquad.esquadbe.storage.service;

import com.esquad.esquadbe.storage.dto.ResponseFileDto;
import com.esquad.esquadbe.storage.entity.FileInfo;
import com.esquad.esquadbe.storage.entity.StoredFile;
import com.esquad.esquadbe.storage.entity.TargetType;
import com.esquad.esquadbe.storage.exception.FileDeleteFailureException;
import com.esquad.esquadbe.storage.exception.FileDownloadFailureException;
import com.esquad.esquadbe.storage.exception.FileIsEmptyException;
import com.esquad.esquadbe.storage.exception.FileNotExistsException;
import com.esquad.esquadbe.storage.exception.FileUploadFailureException;
import com.esquad.esquadbe.storage.repository.StoredFileRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
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
    public ResponseFileDto uploadFile(MultipartFile multipartFile, Long targetId,
        TargetType targetType,
        String userId) {
        if (multipartFile.isEmpty()) {
            throw new FileIsEmptyException();
        }

        FileInfo fileInfo = FileInfo.of(multipartFile.getOriginalFilename(), multipartFile);

        try {
            final var putObjectRequest = getPutObjectRequest(multipartFile,
                fileInfo.getStoredFileName(), urlConverter(targetType));
            final RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new FileUploadFailureException();
        }
        User user = userRepository.findByUsername(userId)
            .orElseThrow(UserNotFoundException::new);

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
        StoredFile storedFile = getFile(storedFileName);

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(urlConverter(storedFile.getTargetType())+storedFileName)
                .build());
        } catch (RuntimeException e) {
            throw new FileDeleteFailureException();
        }

        storedFileRepository.delete(storedFile);
    }

    public byte[] downloadFile(String storedFileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(urlConverter(getFile(storedFileName).getTargetType())+storedFileName)
                .build();

            ResponseBytes<GetObjectResponse> s3Object = s3Client.getObjectAsBytes(getObjectRequest);
            return s3Object.asByteArray();

        } catch (RuntimeException e) {
            throw new FileDownloadFailureException();
        }
    }

    private PutObjectRequest getPutObjectRequest(MultipartFile multipartFile,
        final String fileName, String url) {
        return PutObjectRequest.builder()
            .bucket(bucket)
            .contentType(multipartFile.getContentType())
            .contentLength(multipartFile.getSize())
            .key(url+fileName)
            .build();
    }

    public List<ResponseFileDto> getFileList(Long targetId, TargetType targetType) {
        return storedFileRepository.findAllByTargetIdAndTargetType(targetId, targetType).stream()
            .map(ResponseFileDto::from).toList();
    }

    private String urlConverter(TargetType targetType) {
        return switch (targetType) {
            case QNA -> "qna/";
            case CHAT -> "chat/";
            case TEAM -> "team/";
            case USER -> "user/";
            case STUDY_PAGE -> "study-page/";
        };
    }

    private StoredFile getFile(String storedFileName) {
        return storedFileRepository.findByFileInfo_StoredFileName(storedFileName)
            .orElseThrow(FileNotExistsException::new);
    }
}
