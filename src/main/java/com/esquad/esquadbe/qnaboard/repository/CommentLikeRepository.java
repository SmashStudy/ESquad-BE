package com.esquad.esquadbe.qnaboard.repository;

import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReplyLike;
import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<BookQnaReplyLike, Long> {

    // 특정 사용자가 특정 댓글에 좋아요를 눌렀는지 확인
    BookQnaReplyLike findByUserAndReply(User user, BookQnaReply reply);
}
