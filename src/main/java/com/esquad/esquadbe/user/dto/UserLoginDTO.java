package com.esquad.esquadbe.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
