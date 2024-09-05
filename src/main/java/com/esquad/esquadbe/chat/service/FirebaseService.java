package com.esquad.esquadbe.chat.service;

import com.esquad.esquadbe.chat.firebaseCallback.FirebaseCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public FirebaseService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
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
