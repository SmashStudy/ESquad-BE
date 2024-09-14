package com.esquad.esquadbe.chat.controller;

import com.esquad.esquadbe.chat.dto.ChatEditMessage;
import com.esquad.esquadbe.chat.dto.ChatMessage;
import com.esquad.esquadbe.chat.service.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody ChatMessage chatMsg) {
        Map<String, String> response = new HashMap<>();
        try {
            DatabaseReference roomRef = firebaseService.getReference("MESSAGES/" + chatMsg.getRoomId());
            DatabaseReference messageRef = roomRef.push();  // Firebase에서 메시지 ID 자동 생성
            String newMessageId = messageRef.getKey();
            chatMsg.setMessageId(newMessageId);

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
