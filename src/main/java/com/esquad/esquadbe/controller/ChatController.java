package com.esquad.esquadbe.controller;

import com.esquad.esquadbe.chat.entity.ChatMessage;
import com.esquad.esquadbe.chat.service.FirebaseService;
import com.google.firebase.database.DatabaseReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ChatController {
    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/chat/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody ChatMessage chatMsg) {
        Map<String, String> response = new HashMap<>();
        try {
            DatabaseReference messageRef = firebaseService.getReference("MESSAGES/" + chatMsg.getRoomId() + "/" + chatMsg.getMessageId());
            CompletableFuture<Void> future = new CompletableFuture<>();

            messageRef.setValue(chatMsg, (error, ref) -> {
                if (error != null) {
                    response.put("status", "error");
                    response.put("message", error.getMessage());
                    future.completeExceptionally(error.toException());
                } else {
                    response.put("status", "success");
                    future.complete(null);
                }
            });
            future.join();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
