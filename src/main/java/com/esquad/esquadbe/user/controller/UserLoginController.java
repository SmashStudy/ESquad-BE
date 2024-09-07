package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.user.dto.UserDTO;
import com.esquad.esquadbe.user.dto.UserLoginDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import com.esquad.esquadbe.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@Slf4j
@RestController
public class UserLoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public UserDTO getUserProfile(Principal principal) {
        String username = principal.getName();
        return userService.getUserProfile(username);
    }


    @PostMapping("/loginProc")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername());
        log.info("백엔드로 전달된 아이디: " + userLoginDTO.getUsername());

        if (user != null && bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("Login Successful");
        }

        return ResponseEntity.status(401).body("Login Failed");
    }
}
