package com.esquad.esquadbe.qnaboard.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
@Table(name = "BOOK_QNA_BOARD")
@Entity
@NoArgsConstructor
public class BookQnaBoard extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User writer;  // 'User' 엔티티와의 관계를 설정

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "TEAM_SPACE_ID", nullable = false)
    private TeamSpace teamSpace;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "LIKES")
    private Integer likes;
}
