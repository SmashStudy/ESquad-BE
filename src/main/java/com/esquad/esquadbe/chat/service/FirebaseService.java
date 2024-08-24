package com.esquad.esquadbe.chat.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
}