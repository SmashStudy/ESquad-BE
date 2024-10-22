 package com.esquad.esquadbe.chat.entity;

 import com.esquad.esquadbe.global.entity.BasicEntity;
 import jakarta.persistence.*;
 import lombok.AllArgsConstructor;
 import lombok.Getter;
 import lombok.NoArgsConstructor;

 @Entity
 @Table (name = "CHATFILE")
 @Getter
 @AllArgsConstructor
 @NoArgsConstructor
 public class ChatS3FileEntity extends BasicEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "ID")
     private Long fileId;

     @Column(name = "USERNAME" , nullable = false, length = 20)
     private String username;

     @Column(name = "ORIGINAL_FILENAME", nullable = false, length = 100)
     private String originalFileName;

     @Column(name = "FILENAME", nullable = false, length = 100)
     private String fileName;

     @Column(name = "FILEURL", nullable = false)
     private String fileUrl;
 }
