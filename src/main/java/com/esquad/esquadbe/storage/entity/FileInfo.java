package com.esquad.esquadbe.storage.entity;

import com.esquad.esquadbe.storage.util.FileNameUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class FileInfo {

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String contentType;

    public static FileInfo of(final String originalFileName, final MultipartFile file) {
        String extension = FileNameUtils.getExtension(originalFileName);
        String baseStoredFileName = FileNameUtils.getBaseFileName(originalFileName);
        String storedFileName = FileNameUtils.buildUniqueFileName(baseStoredFileName, extension);

        return FileInfo.builder()
            .originalFileName(originalFileName)
            .storedFileName(storedFileName)
            .extension(extension)
            .fileSize(file.getSize())
            .contentType(file.getContentType())
            .build();
    }
}
