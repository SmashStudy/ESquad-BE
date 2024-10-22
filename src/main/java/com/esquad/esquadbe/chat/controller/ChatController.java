package com.esquad.esquadbe.chat.controller;

import com.esquad.esquadbe.chat.dto.ChatEditMessage;
import com.esquad.esquadbe.chat.dto.ChatMessage;
import com.esquad.esquadbe.chat.exception.ChatAccessException;
import com.esquad.esquadbe.chat.exception.ChatException;
import com.esquad.esquadbe.chat.service.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
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
            DatabaseReference messageRef = roomRef.push();
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
                    response.put("messageId", newMessageId);
                    future.complete(null);
                }
            });
            future.join();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ChatException();
        }
    }

    @GetMapping("/messages/{roomId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getMessages(@PathVariable String roomId) {
        Map<String, Object> response = new HashMap<>();
        CompletableFuture<ResponseEntity<Map<String, Object>>> futureResponse = new CompletableFuture<>();

        DatabaseReference messagesRef = firebaseService.getReference("MESSAGES/" + roomId);
        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map<String, Object>> messages = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Map<String, Object> message = (Map<String, Object>) child.getValue();
                        if (message != null) {
                            message.put("id", child.getKey());
                            messages.add(message);
                        }
                    }
                    response.put("messages", messages);
                    response.put("status", "success");
                } else {
                    response.put("messages", new ArrayList<>());
                    response.put("status", "success");
                }
                futureResponse.complete(ResponseEntity.ok(response));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new ChatException();
            }
        });
        return futureResponse;
    }

    @PutMapping("/{roomId}/{messageId}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> updateMessage(
            @PathVariable String roomId,
            @PathVariable String messageId,
            @RequestBody ChatEditMessage request,
            Principal principal) {

        Map<String, String> response = new HashMap<>();
        CompletableFuture<ResponseEntity<Map<String, String>>> futureResponse = new CompletableFuture<>();

        DatabaseReference messageRef = firebaseService.getReference("MESSAGES/" + roomId + "/" + messageId);
        String username = principal.getName();

        messageRef.child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String originalUsername = dataSnapshot.getValue(String.class);

                if (originalUsername != null && originalUsername.equals(username)) {
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
                    throw new ChatAccessException();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               throw new ChatException();
            }
        });
        return futureResponse;
    }

    @DeleteMapping("/{roomId}/{messageId}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deleteMessage(
            @PathVariable String roomId,
            @PathVariable String messageId,
            Principal principal) {

        DatabaseReference messageRef = firebaseService.getReference("MESSAGES/" + roomId + "/" + messageId);
        CompletableFuture<ResponseEntity<Map<String, String>>> futureResponse = new CompletableFuture<>();
        Map<String, String> response = new HashMap<>();

        String username = principal.getName();

        messageRef.child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String originalUsername = dataSnapshot.getValue(String.class);

                if (originalUsername != null && originalUsername.equals(username)) {
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
                throw new ChatAccessException();
            }
        });
        return futureResponse;
    }
}
