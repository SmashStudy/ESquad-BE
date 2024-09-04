package com.esquad.esquadbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
    private LocalDate birthDay;
    private String address;
}
