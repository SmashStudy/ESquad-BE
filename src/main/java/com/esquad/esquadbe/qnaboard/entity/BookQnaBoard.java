package com.esquad.esquadbe.qnaboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.awt.print.Book;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "book_qna_board")
public class BookQnaBoard {

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

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @Column(name = "LIKES")
    private Integer likes;

}
