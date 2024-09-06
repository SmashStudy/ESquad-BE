package com.esquad.esquadbe.qnaboard.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "BOOK_QNA_BOARD")
public class BookQnaBoard extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "WRITER", nullable = false)
    private String writer;

    @Column(name = "BOOK", nullable = false)
    private String book;

    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "LIKES")
    private Integer likes;
}