package com.esquad.esquadbe.controller;

import com.esquad.esquadbe.chat.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    private FirebaseService firebaseService;

}
