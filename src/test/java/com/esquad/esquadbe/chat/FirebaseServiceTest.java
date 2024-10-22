package com.esquad.esquadbe.chat;

import com.esquad.esquadbe.chat.service.FirebaseService;
import com.google.firebase.database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FirebaseServiceTest {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference mockDatabaseReference;

    @BeforeEach
    void setUp() {
        mockDatabaseReference = Mockito.mock(DatabaseReference.class);
    }

    @Test
    void testSendMessage() throws InterruptedException {
        String teamId = "team1";
        String roomId = "room1";
        String messageId = "message1";
        String messageContent = "Hello, Firebase!";
        long timestamp = System.currentTimeMillis();

        // Principal 객체를 목(mock)으로 생성
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("user1"); // 사용자 ID를 목으로 설정

        // Principal을 사용하여 메시지를 전송
        firebaseService.sendMessage(teamId, roomId, principal, messageId, messageContent, timestamp);

        // 메시지가 제대로 저장되었는지 확인
        CountDownLatch latch = new CountDownLatch(1);
        firebaseService.receiveMessage(teamId, roomId, messageId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String storedMessageContent = dataSnapshot.child("messageContent").getValue(String.class);
                assertThat(storedMessageContent).isEqualTo(messageContent);
                latch.countDown(); // 데이터가 일치하면 카운트 다운
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown(); // 실패 시에도 카운트 다운하여 테스트 종료
            }
        });

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testEditMessage() throws InterruptedException {
        String teamId = "team1";
        String roomId = "room1";
        String messageId = "message1";
        String newMessageContent = "Edited Message!";
        long newTimestamp = System.currentTimeMillis();

        // Principal 객체를 목(mock)으로 생성
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("user1"); // 사용자 ID를 목으로 설정

        // 목 객체에서 사용자 ID를 가져옴
        String userId = principal.getName();
        firebaseService.editMessage(teamId, roomId, principal, messageId, newMessageContent, newTimestamp);

        // 메시지가 제대로 수정되었는지 확인
        CountDownLatch latch = new CountDownLatch(1);
        firebaseService.receiveMessage(teamId, roomId, messageId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String updatedMessageContent = dataSnapshot.child("messageContent").getValue(String.class);
                assertThat(updatedMessageContent).isEqualTo(newMessageContent);
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testDeleteMessage() throws InterruptedException {
        String roomId = "room1";
        String messageId = "message1";

        // 메시지 삭제
        firebaseService.deleteMessage(roomId, messageId);

        // 메시지가 제대로 삭제되었는지 확인
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference messageRef = firebaseService.getReference("MESSAGES/" + roomId + "/" + messageId);
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assertThat(dataSnapshot.exists()).isFalse(); // 삭제 후 데이터가 존재하지 않는지 확인
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
    }
}
