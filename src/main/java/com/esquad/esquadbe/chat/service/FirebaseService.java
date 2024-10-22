package com.esquad.esquadbe.chat.service;

import com.esquad.esquadbe.chat.firebaseCallback.FirebaseCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FirebaseService {

    private final DatabaseReference databaseReference;

    @Autowired
    public FirebaseService(FirebaseDatabase firebaseDatabase) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public DatabaseReference getReference(String path) {
        return databaseReference.child(path);
    }

    public void sendMessage(String teamId, String roomId, Principal principal , String messageId, String messageContent, long timestamp) {
        DatabaseReference messageRef = getReference("CHAT_ROOMS/" + teamId + "/" + roomId + "/messages/" + messageId);
        String username = principal.getName();
        Map<String, Object> message = new HashMap<>();
        message.put("userId", username);
        message.put("messageId", messageId);
        message.put("messageContent", messageContent);
        message.put("timestamp", timestamp);

        messageRef.setValueAsync(message);
    }

    public void receiveMessage(String teamId, String roomId, String messageId, ValueEventListener listener) {
        DatabaseReference messageRef = getReference("CHAT_ROOMS/" + teamId + "/" + roomId + "/messages/" + messageId);
        messageRef.addListenerForSingleValueEvent(listener);
    }

    public void editMessage(String teamId, String roomId, Principal principal, String messageId, String newMessageContent, long newTimestamp) {
        DatabaseReference messageRef = getReference("CHAT_ROOMS/" + teamId + "/" + roomId + "/messages/" + messageId);
        String username = principal.getName();
        Map<String, Object> updates = new HashMap<>();
        updates.put("messageContent", newMessageContent);
        updates.put("timestamp", newTimestamp);

        messageRef.updateChildrenAsync(updates);
    }

    public void deleteMessage(String roomId, String messageId) {
        DatabaseReference messageRef = getReference("MESSAGES/" + roomId + "/" + messageId);
        messageRef.removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Message deletion failed: " + databaseError.getMessage());
            } else {
                System.out.println("Message successfully deleted.");
            }
        });
    }
}