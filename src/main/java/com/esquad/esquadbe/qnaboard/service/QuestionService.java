package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.dto.QnaRequestDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.exception.UnauthorizedException;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.storage.entity.TargetType;
import com.esquad.esquadbe.storage.service.S3FileService;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final BookRepository bookRepository;
    private final TeamSpaceRepository teamSpaceRepository;
    private final S3FileService s3FileService;

    private User getUser(String username) {
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(UserNotFoundException::new);
        } catch (UserNotFoundException e) {
            log.error("[UserNotFoundException] username: {}, message: {}", username, e.getMessage());
            throw e;
        }
    }

    private Book getBook(Long bookId) {
        try {
            return bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        } catch (ResourceNotFoundException e) {
            log.error("[ResourceNotFoundException] bookId: {}, message: {}", bookId, e.getMessage());
            throw e;
        }
    }

    private TeamSpace getTeamspace(Long teamSpaceId) {
        try {
            return teamSpaceRepository.findById(teamSpaceId)
                    .orElseThrow(() -> new ResourceNotFoundException("TeamSpace not found with id: " + teamSpaceId));
        } catch (ResourceNotFoundException e) {
            log.error("[ResourceNotFoundException] teamSpaceId: {}, message: {}", teamSpaceId, e.getMessage());
            throw e;
        }
    }

    public Page<QnaBoardResponseDTO> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookQnaBoard> questionPage = questionRepository.findAll(pageable);
        log.info("[getAllQuestions] page: {}, size: {}", page, size);
        return questionPage.map(QnaBoardResponseDTO::from);
    }

    public QnaBoardResponseDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(QnaBoardResponseDTO::from)
                .orElseThrow(() -> {
                    log.error("[ResourceNotFoundException] questionId: {}", id);
                    return new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id);
                });
    }

    @Transactional
    public QnaBoardResponseDTO createQuestion(QnaRequestDTO qnaForm, MultipartFile file) {

        String username = qnaForm.username().getName();
        User user = getUser(username);
        Book book = getBook(qnaForm.bookId());
        TeamSpace teamSpace = getTeamspace(qnaForm.teamSpaceId());

        BookQnaBoard newBoard = qnaForm.to(user, book, teamSpace);
        BookQnaBoard savedQuestion = questionRepository.save(newBoard);
        log.info("[createQuestion] new question created by user: {}", username);

        if (file != null && !file.isEmpty()) {
            log.info("[createQuestion] file uploaded for questionId: {}", savedQuestion.getId());
            s3FileService.uploadFile(file, savedQuestion.getId(), TargetType.QNA, username);
        }

        return QnaBoardResponseDTO.from(savedQuestion);
    }

    public Page<QnaBoardResponseDTO> getQuestionsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        log.info("[getQuestionsByTitle] title: {}, page: {}, size: {}", title, page, size);
        return questionRepository.findByTitleContaining(title, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    public Page<QnaBoardResponseDTO> getQuestionsByWriter(String username, int page, int size) {
        User user = getUser(username);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        log.info("[getQuestionsByWriter] username: {}, page: {}, size: {}", username, page, size);
        return questionRepository.findByWriter(user, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    @Transactional
    public QnaBoardResponseDTO updateQuestion(Long id, QnaRequestDTO qnaForm, Principal principal) {
        BookQnaBoard existBoard = questionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[ResourceNotFoundException] questionId: {}", id);
                    return new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id);
                });

        if (!existBoard.getWriter().getUsername().equals(principal.getName())) {
            log.error("[UnauthorizedException] user: {} does not have permission to update questionId: {}", principal.getName(), id);
            throw new UnauthorizedException("게시글 수정 권한이 없습니다.");
        }

        Book updatedBook = qnaForm.bookId() != null ? getBook(qnaForm.bookId()) : existBoard.getBook();

        BookQnaBoard updatedBoard = BookQnaBoard.builder()
                .id(existBoard.getId())
                .title(qnaForm.title())
                .content(qnaForm.content())
                .writer(existBoard.getWriter())
                .book(updatedBook)
                .teamSpace(existBoard.getTeamSpace())
                .likes(existBoard.getLikes())
                .build();

        log.info("[updateQuestion] questionId: {} updated by user: {}", id, principal.getName());
        return QnaBoardResponseDTO.from(questionRepository.save(updatedBoard));
    }

    @Transactional
    public void deleteQuestion(Long questionId, Principal principal) {
        BookQnaBoard question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + questionId));

        if (!question.getWriter().getUsername().equals(principal.getName())) {
            throw new UnauthorizedException("게시글 삭제 권한이 없습니다.");
        }

        questionRepository.delete(question);
    }
}
