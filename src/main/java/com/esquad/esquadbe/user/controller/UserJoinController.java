package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.service.UserJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final UserJoinService userJoinService;

    @GetMapping("/join")
    public String joinPage() {
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