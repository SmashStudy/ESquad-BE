package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.CommentRequestDTO;
import com.esquad.esquadbe.qnaboard.dto.CommentResponseDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaReply;
import com.esquad.esquadbe.qnaboard.exception.CommentNotFoundException;
import com.esquad.esquadbe.qnaboard.exception.QuestionNotFoundException;
import com.esquad.esquadbe.qnaboard.repository.CommentRepository;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import com.esquad.esquadbe.user.exception.UserUsernameException;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public CommentResponseDTO createComment(Long boardId, CommentRequestDTO requestDTO, String username) {

        User user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(UserNotFoundException::new);

        BookQnaBoard board = questionRepository.findById(boardId)
                .orElseThrow(QuestionNotFoundException::new);

        BookQnaReply newComment = requestDTO.toEntity(user, board);
        BookQnaReply savedComment = commentRepository.save(newComment);

        return CommentResponseDTO.from(savedComment);
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO requestDTO, String username) {

        User user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(UserNotFoundException::new);


        BookQnaReply comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getWriter().getId().equals(user.getId())) {
            throw new UserUsernameException();
        }

        comment.setContent(requestDTO.content());
        comment.setReplyFlag(requestDTO.replyFlag());

        return CommentResponseDTO.from(comment);
    }


    @Transactional
    public void deleteComment(Long commentId, String username) {

        User user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(UserNotFoundException::new);


        BookQnaReply comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getWriter().getId().equals(user.getId())) {
            throw new UserUsernameException();
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponseDTO> getCommentsByBoardId(Long boardId) {

        BookQnaBoard board = questionRepository.findById(boardId)
                .orElseThrow(QuestionNotFoundException::new);

        return commentRepository.findByBoard(board).stream()
                .map(CommentResponseDTO::from)
                .collect(Collectors.toList());
    }
}
