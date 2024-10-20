package com.esquad.esquadbe.storage.dto;

import com.esquad.esquadbe.storage.entity.StoredFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseFileDto {

    // 노출 X
    private Long id;
    private String storedFileName;

    private String originalFileName;
    private String userNickname;
    private String createdAt;
    private Long fileSize;

    public static ResponseFileDto from(StoredFile storedFile) {
        return ResponseFileDto.builder()
            .id(storedFile.getId())
            .storedFileName(storedFile.getFileInfo().getStoredFileName())
            .originalFileName(storedFile.getFileInfo().getOriginalFileName())
            .userNickname(storedFile.getUser().getNickname())
            .createdAt(storedFile.getCreatedAt().toString())
            .fileSize(storedFile.getFileInfo().getFileSize())
            .build();
    }
}
