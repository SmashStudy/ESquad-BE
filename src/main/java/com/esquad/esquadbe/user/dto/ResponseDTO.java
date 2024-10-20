package com.esquad.esquadbe.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ResponseDTO {
    
    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

}
