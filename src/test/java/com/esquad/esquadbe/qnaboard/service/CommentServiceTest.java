package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.CommentRequestDTO;
import com.esquad.esquadbe.qnaboard.dto.CommentResponseDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.exception.CommentNotFoundException;
import com.esquad.esquadbe.qnaboard.repository.CommentRepository;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserUsernameException;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private BookQnaReply existingComment;
    private User user;
    private BookQnaBoard board;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .username("testUser1")
                .nickname("Test Nickname")
                .password("password")
                .email("test@example.com")
                .phoneNumber("01012345678")
                .birthDay(LocalDate.of(1990, 1, 1))
                .address("Test Address")
                .build());

        board = questionRepository.save(BookQnaBoard.builder()
                .title("Test Title")
                .content("Test Content")
                .writer(user)
                .likes(0)
                .build());

        existingComment = commentRepository.save(BookQnaReply.builder()
                .content("Test Comment")
                .writer(user)
                .board(board)
                .likes(0)
                .depth(0)
                .orderNo(0)
                .deletedFlag(false)
                .build());
    }

    @Test
    @DisplayName("작성자가 댓글 수정 성공")
    void updateCommentSuccess() {
        // Given
        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
                .content("Updated Comment")
                .replyFlag(false)
                .build();

        // When
        CommentResponseDTO updatedComment = commentService.updateComment(existingComment.getId(), requestDTO, user.getId().toString());

        // Then
        assertNotNull(updatedComment);
        assertEquals("Updated Comment", updatedComment.getContent());
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 댓글 수정 시도")
    void updateCommentUnauthorized() {
        // Given: 다른 유저가 댓글을 수정하려고 시도
        User otherUser = userRepository.save(User.builder()
                .username("otherUser")
                .nickname("Other Nickname")
                .password("password")
                .email("other@example.com")
                .phoneNumber("01098765432")
                .birthDay(LocalDate.of(1995, 5, 5))
                .address("Other Address")
                .build());

        CommentRequestDTO requestDTO = CommentRequestDTO.builder()
                .content("Unauthorized Update")
                .replyFlag(false)
                .build();

        // When & Then: UserUsernameException이 발생해야 함
        UserUsernameException exception = assertThrows(UserUsernameException.class, () -> {
            commentService.updateComment(existingComment.getId(), requestDTO, otherUser.getId().toString());
        });

        assertEquals("User not exists", exception.getMessage());
    }

    @Test
    @DisplayName("작성자가 댓글 삭제 성공")
    void deleteCommentSuccess() {
        // When
        commentService.deleteComment(existingComment.getId(), user.getId().toString());

        // Then
        assertFalse(commentRepository.findById(existingComment.getId()).isPresent());
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 댓글 삭제 시도")
    void deleteCommentUnauthorized() {
        // Given: 다른 유저가 댓글을 삭제하려고 시도
        User otherUser = userRepository.save(User.builder()
                .username("otherUser")
                .nickname("Other Nickname")
                .password("password")
                .email("other@example.com")
                .phoneNumber("01098765432")
                .birthDay(LocalDate.of(1995, 5, 5))
                .address("Other Address")
                .build());

        // When & Then: UserUsernameException이 발생해야 함
        UserUsernameException exception = assertThrows(UserUsernameException.class, () -> {
            commentService.deleteComment(existingComment.getId(), otherUser.getId().toString());
        });

        assertEquals("User not exists", exception.getMessage());
    }

    @Test
    @DisplayName("댓글을 찾을 수 없을 때 예외 발생")
    void deleteCommentNotFound() {
        // Given: 존재하지 않는 댓글 ID
        Long nonExistingCommentId = 999L;

        // When & Then: CommentNotFoundException이 발생해야 함
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> {
            commentService.deleteComment(nonExistingCommentId, user.getId().toString());
        });

        assertEquals("Comment doesn't exist", exception.getMessage());
    }
}
