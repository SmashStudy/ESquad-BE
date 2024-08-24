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

    public String createChatRoom(String roomName) {
        DatabaseReference roomsRef = firebaseService.getReference("ROOMS");
        String roomId = roomsRef.push().getKey();

        Map<String, Object> roomData = new HashMap<>();
        roomData.put("roomName", roomName);
        roomData.put("timestamp", ServerValue.TIMESTAMP);

        roomsRef.child(roomId).setValue(roomData, (error, ref) -> {
            if (error != null) {
                System.err.println("Error creating chat room: " + error.getMessage());
            } else {
                System.out.println("Chat room created successfully");
            }
        });
        return roomId;
    }
}
