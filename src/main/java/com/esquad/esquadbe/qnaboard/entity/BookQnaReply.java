package com.esquad.esquadbe.qnaboard.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "BOOK_QNA_REPLY")
public class BookQnaReply extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private BookQnaBoard board;

    @ManyToOne
    @JoinColumn(name = "PARENT_REPLY_ID")
    private BookQnaReply parentReply;

    @ManyToOne
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private User writer;

    @Column(name = "REPLY_FLAG", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean replyFlag;

    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    @Column(name = "DEPTH")
    private Integer depth;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @Column(name = "LIKES")
    private Integer likes;

    @Column(name = "DELETED_FLAG", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean deletedFlag;

    @OneToMany(mappedBy = "parentReply", orphanRemoval = true)
    private List<BookQnaReply> childReplies;
}