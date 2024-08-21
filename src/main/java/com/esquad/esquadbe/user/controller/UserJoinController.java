package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.service.UserJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final UserJoinService userJoinService;

    @GetMapping
    public String joinP() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(@RequestBody UserJoinDTO userJoinDTO) {
        try {
            userJoinService.joinProcess(userJoinDTO);
            return "회원가입 성공";
        } catch (Exception e) {
            return "회원가입 실패: " + e.getMessage();
        }
    }

}
