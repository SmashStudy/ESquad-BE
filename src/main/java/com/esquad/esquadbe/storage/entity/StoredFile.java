package com.esquad.esquadbe.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STORED_FILES")
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

//    @ManyToOne
//    private User user;

    private String originFileName;

    private String storedFileName;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private FileType FileType;

    private Integer fileSize;



}
