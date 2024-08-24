package com.esquad.esquadbe.chat.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatMessageService {
    private final FirebaseService firebaseService;

    @Autowired
    public ChatMessageService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
}
