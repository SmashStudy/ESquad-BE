package com.esquad.esquadbe.storage.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "STORED_FILES")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StoredFile extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    private String originFileName;

    private String storedFileName;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private Integer fileSize;



}
