package com.esquad.esquadbe.chat.service;

import com.esquad.esquadbe.chat.firebaseCallback.FirebaseCallback;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseService {

    @Value("${firebase.service-account-path}")
    private String serviceAccountPath;

    @Value("${firebase.database-url}")
    private String databaseUrl;

    private FirebaseDatabase firebaseDatabase;

    @PostConstruct
    private void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            firebaseDatabase = FirebaseDatabase.getInstance();
        } catch (IOException e) {
            System.err.println("Firebase initialization error: " + e.getMessage());
        }
    }

    public DatabaseReference getReference(String path) {
        return firebaseDatabase.getReference(path);
    }

    public void updateMessage(String roomId, String messageId, String userId, String newMessage, FirebaseCallback callback) {
        DatabaseReference messageRef = getReference("MESSAGES/" + roomId + "/" + messageId);
        messageRef.child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String originalUserId = dataSnapshot.getValue(String.class);

                if (originalUserId != null && originalUserId.equals(userId)) {
                    messageRef.child("message").setValue(newMessage, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            callback.onFailure(databaseError.toException());
                        } else {
                            callback.onSuccess();
                        }
                    });
                } else {
                    callback.onFailure(new Exception("Unauthorized"));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
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
