package com.esquad.esquadbe.chat.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "CHATFILE")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class S3FileEntity extends BasicEntity {
    @Id
    @Column(name = "USERNAME" , nullable = false, length = 20)
    private String userName;

    @Column(name = "ORIGINAL_FILENAME", nullable = false, length = 100)
    private String originalFilename;

    @Column(name = "FILENAME", nullable = false, length = 100)
    private String fileName;

}
