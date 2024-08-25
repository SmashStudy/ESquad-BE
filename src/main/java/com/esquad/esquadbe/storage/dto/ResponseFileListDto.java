package com.esquad.esquadbe.storage.dto;

import com.esquad.esquadbe.storage.entity.FileInfo;
import com.esquad.esquadbe.storage.entity.StoredFile;
import com.esquad.esquadbe.storage.entity.TargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseFileListDto {

    private Long id;
    private Long targetId;
    private TargetType targetType;
    private Long userId;
    private FileInfo fileInfo;

    public static ResponseFileListDto from(StoredFile storedFile) {
        return new ResponseFileListDto(
            storedFile.getId(),
            storedFile.getTargetId(),
            storedFile.getTargetType(),
            storedFile.getUser().getId(),
            storedFile.getFileInfo()
        );
    }
}
