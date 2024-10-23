package com.esquad.esquadbe.smtp.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MailDTO {
    private String email;
    private String number;
    private String username;

    private String newPassword;

    private String confirmPassword;
}