package com.esquad.esquadbe.qnaboard.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.awt.print.Book;
import java.time.LocalDateTime;


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

    @ManyToOne
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private User writer;

    @ManyToOne
    @JoinColumn(name = "STUDY_PAGE_ID")
    private StudyPage studyPage;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "LIKES")
    private Integer likes;

}
