package com.esquad.esquadbe.qnaboard.repository;


import com.esquad.esquadbe.qnaboard.entity.BookQnaReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<BookQnaReplyLike, Long> {

    // 특정 사용자가 특정 댓글에 좋아요를 눌렀는지 확인
    Optional<BookQnaReplyLike> findByUserIdAndReplyId(Long userId, Long replyId);

    // 특정 댓글의 좋아요 수 반환
    Long countByReplyId(Long replyId);
}