package com.esquad.esquadbe.chat.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final FirebaseService firebaseService;

    public String createChatRoom(String roomName) {
        DatabaseReference roomsRef = firebaseService.getReference("ROOMS");
        String roomId = roomsRef.push().getKey();

        Map<String, Object> roomData = new HashMap<>();
        roomData.put("roomName", roomName);
        roomData.put("timestamp", ServerValue.TIMESTAMP);

        roomsRef.child(roomId).setValue(roomData, (error, ref) -> {
            if (error != null) {
                log.error(error.getMessage());
            } else {
                System.out.println("Chat room created successfully");
            }
        });
        return roomId;
    }
    public void addUserToRoom(String roomId, String userId) {
        DatabaseReference roomUsersRef = firebaseService.getReference("ROOM_USERS/" + roomId);
        roomUsersRef.child(userId).setValue(true, (error, ref) -> {
            if (error != null) {
               log.error(error.getMessage());
            } else {
                System.out.println("User added to room successfully");
            }
        });
    }
    public void saveMessage(String roomId, String userId, String message, String type, String status) {
        DatabaseReference messagesRef = firebaseService.getReference("MESSAGES/" + roomId);
        String messageId = messagesRef.push().getKey();

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("messageId", messageId);
        messageData.put("userId", userId);
        messageData.put("message", message);
        messageData.put("timestamp", ServerValue.TIMESTAMP);
        messageData.put("type", type);
        messageData.put("status", status);

        messagesRef.child(messageId).setValue(messageData, (error, ref) -> {
            if (error != null) {
               log.error(error.getMessage());
            } else {
                System.out.println("Message saved successfully");
            }
        });
    }
    public void getMessages(String roomId, ValueEventListener listener) {
        DatabaseReference messagesRef = firebaseService.getReference("MESSAGES/" + roomId);
        messagesRef.addListenerForSingleValueEvent(listener);
    }
}
