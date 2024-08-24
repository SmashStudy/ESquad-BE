package com.esquad.esquadbe.controller;

import com.esquad.esquadbe.chat.entity.ChatEditMessage;
import com.esquad.esquadbe.chat.entity.ChatMessage;
import com.esquad.esquadbe.chat.service.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/chat/messages/{roomId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getMessages(@PathVariable String roomId) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<ResponseEntity<Map<String, Object>>> futureResponse = new CompletableFuture<>();

        DatabaseReference messagesRef = firebaseService.getReference("MESSAGES/" + roomId);
        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.put("messages", dataSnapshot.getValue());
                response.put("status", "success");
                futureResponse.complete(ResponseEntity.ok(response));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                response.put("status", "error");
                response.put("message", databaseError.getMessage());
                futureResponse.complete(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
            }
        });
        return futureResponse;
    }
    @PutMapping("/chat/edit/{roomId}/{messageId}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> updateMessage(
            @PathVariable String roomId,
            @PathVariable String messageId,
            @RequestBody ChatEditMessage request) {

        Map<String, String> response = new HashMap<>();
        CompletableFuture<ResponseEntity<Map<String, String>>> futureResponse = new CompletableFuture<>();

        DatabaseReference messageRef = firebaseService.getReference("MESSAGES/" + roomId + "/" + messageId);

        messageRef.child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String originalUserId = dataSnapshot.getValue(String.class);

                if (originalUserId != null && originalUserId.equals(request.getUserId())) {
                    messageRef.child("message").setValue(request.getNewMessage(), (error, ref) -> {
                        if (error != null) {
                            response.put("status", "error");
                            response.put("message", error.getMessage());
                            futureResponse.complete(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                        } else {
                            response.put("status", "success");
                            futureResponse.complete(ResponseEntity.ok(response));
                        }
                    });
                } else {
                    response.put("status", "error");
                    response.put("message", "Unauthorized");
                    futureResponse.complete(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                response.put("status", "error");
                response.put("message", databaseError.getMessage());
                futureResponse.complete(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
            }
        });
        return futureResponse;
    }
    @DeleteMapping("/chat/delete/{roomId}/{messageId}")
    public ResponseEntity<Map<String, String>> deleteMessage(
            @PathVariable String roomId,
            @PathVariable String messageId,
            @RequestBody Map<String, String> request) {

        String userId = request.get("userId");

        DatabaseReference messageRef = firebaseService.getReference("MESSAGES/" + roomId + "/" + messageId);

        CompletableFuture<ResponseEntity<Map<String, String>>> futureResponse = new CompletableFuture<>();
        Map<String, String> response = new HashMap<>();

        messageRef.child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String originalUserId = dataSnapshot.getValue(String.class);

                if (originalUserId != null && originalUserId.equals(userId)) {
                    firebaseService.deleteMessage(roomId, messageId); // 메시지 삭제
                    response.put("status", "success");
                    futureResponse.complete(ResponseEntity.ok(response));
                } else {
                    response.put("status", "error");
                    response.put("message", "Unauthorized");
                    futureResponse.complete(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                response.put("status", "error");
                response.put("message", databaseError.getMessage());
                futureResponse.complete(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
            }
        });
        return futureResponse.join();
    }
}
