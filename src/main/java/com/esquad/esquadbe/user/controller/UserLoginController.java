package com.esquad.esquadbe.user.controller;

import com.esquad.esquadbe.user.dto.UserLoginDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;



@RestController
public class UserLoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/profile")
    public String getUserProfile(Principal principal) {
        return principal.getName();
    }


    @PostMapping("/loginProc")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {

        User user = userRepository.findByUsername(userLoginDTO.getUsername());

        if (user != null && bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("Login Successful");
        }

        return ResponseEntity.status(401).body("Login Failed");
    }
}
