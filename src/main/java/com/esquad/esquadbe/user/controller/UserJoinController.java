package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.service.UserJoinService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserJoinController {

    private final UserJoinService userJoinService;

    @PostMapping
    public String joinProcess(@RequestBody @NotNull UserJoinDTO userJoinDTO) {
        try {
            userJoinService.joinProcess(userJoinDTO);
            return "회원가입 성공";
        } catch (Exception e) {
            return "회원가입 실패: " + e.getMessage();
        }
    }

    @GetMapping("/usernamecheck")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean existsByUsername = userJoinService.checkUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", !existsByUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usernicknamecheck")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean existsByNickname = userJoinService.checkNickname(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", !existsByNickname);
        return ResponseEntity.ok(response);
    }
}