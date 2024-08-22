package com.esquad.esquadbe.studypage.entity;
import com.esquad.esquadbe.global.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "BOOKS")
public class Book extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 150)
    private String title;

    @Column(name = "IMG_PATH", nullable = false, length = 200)
    private String imgPath;

    @Column(name = "AUTHOR", nullable = false, length = 50)
    private String author;

    @Column(name = "PUBLISHER", nullable = false, length = 50)
    private String publisher;

    @Column(name = "PUB_DATE", nullable = false, length = 8)
    private String pubDate;

    @Column(name = "ISBN", nullable = false, length = 15)
    private String isbn;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;
}
