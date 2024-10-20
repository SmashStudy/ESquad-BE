package com.esquad.esquadbe.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ChatS3FileDto {
    private Long fileId;
    private String userName;
    private String originalFileName;
    private String fileName;
    private String fileUrl;
    private LocalDate uploadDate;
}
