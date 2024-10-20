package com.esquad.esquadbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}
