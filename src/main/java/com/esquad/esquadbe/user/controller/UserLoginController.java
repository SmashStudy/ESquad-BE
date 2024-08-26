package com.esquad.esquadbe.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserLoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/profile")
    public String getUserProfile(Principal principal) {
        return principal.getName(); // Principal 객체를 사용하여 유저네임을 반환
    }


}