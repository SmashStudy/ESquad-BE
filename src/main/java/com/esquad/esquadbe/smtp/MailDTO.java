package com.esquad.esquadbe.smtp;

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

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!~&+@])[a-z\\d!~&+@]{6,16}$",
            message = "비밀번호는 소문자, 숫자, 특수문자(!~&+@)를 포함한 6~16자여야 합니다."
    )
    private String newPassword;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!~&+@])[a-z\\d!~&+@]{6,16}$",
            message = "비밀번호는 소문자, 숫자, 특수문자(!~&+@)를 포함한 6~16자여야 합니다."
    )
    private String confirmPassword;
}