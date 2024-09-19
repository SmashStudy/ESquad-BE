package com.esquad.esquadbe.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class S3FileDto {
    private String userName;
    private String originalFileName;
    private String fileName;
    private LocalDate uploadDate;
}
