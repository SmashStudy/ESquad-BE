package com.esquad.esquadbe.chat.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final FirebaseService firebaseService;

    public String createChatRoom(String roomName) {
        try {
            DatabaseReference roomsRef = firebaseService.getReference("ROOMS");
            String roomId = roomsRef.push().getKey();

            Map<String, Object> roomData = new HashMap<>();
            roomData.put("roomName", roomName);
            roomData.put("timestamp", ServerValue.TIMESTAMP);

            roomsRef.child(roomId).setValue(roomData, (error, ref) -> {
                if (error != null) {
                    log.error("Failed to create chat room: {}", error.getMessage());
                } else {
                    log.info("Chat room created successfully with ID: {}", roomId);
                }
            });
            return roomId;
        } catch (Exception e) {
            log.error("Error creating chat room", e);
            throw new RuntimeException("Error creating chat room", e);
        }
    }
    public void addUserToRoom(String roomId, String userId) {
        try {
            DatabaseReference roomUsersRef = firebaseService.getReference("ROOM_USERS/" + roomId);
            roomUsersRef.child(userId).setValue(true, (error, ref) -> {
                if (error != null) {
                    log.error("Failed to add user to room: {}", error.getMessage());
                } else {
                    log.info("User {} added to room {} successfully", userId, roomId);
                }
            });
        } catch (Exception e) {
            log.error("Error adding user to room", e);
            throw new RuntimeException("Error adding user to room", e);
        }
    }
    public void saveMessage(String roomId, String userId, String message, String type, String status) {
        try {
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
                    log.error("Failed to save message: {}", error.getMessage());
                } else {
                    log.info("Message {} saved successfully in room {}", messageId, roomId);
                }
            });
        } catch (Exception e) {
            log.error("Error saving message", e);
            throw new RuntimeException("Error saving message", e);
        }
    }
    public void getMessages(String roomId, ValueEventListener listener) {
        try {
            DatabaseReference messagesRef = firebaseService.getReference("MESSAGES/" + roomId);
            messagesRef.addListenerForSingleValueEvent(listener);
        } catch (Exception e) {
            log.error("Error getting messages", e);
            throw new RuntimeException("Error getting messages", e);
        }
    }
}
