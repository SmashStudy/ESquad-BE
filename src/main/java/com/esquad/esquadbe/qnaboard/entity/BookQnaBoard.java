package com.esquad.esquadbe.qnaboard.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @JoinColumn(name = "TEAM_SPACE_ID")
    private TeamSpace teamSpace;

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
