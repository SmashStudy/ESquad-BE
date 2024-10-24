package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.dto.QnaRequestDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.exception.BookNotFoundException;
import com.esquad.esquadbe.qnaboard.exception.QuestionNotFoundException;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.storage.entity.TargetType;
import com.esquad.esquadbe.storage.service.S3FileService;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import com.esquad.esquadbe.user.exception.UserUsernameException;
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
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
    }

    private TeamSpace getTeamspace(Long teamSpaceId) {
        return teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(TeamNotFoundException::new);
    }

    public Page<QnaBoardResponseDTO> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookQnaBoard> questionPage = questionRepository.findAll(pageable);
        return questionPage.map(QnaBoardResponseDTO::from);
    }

    public QnaBoardResponseDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(QnaBoardResponseDTO::from)
                .orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    public QnaBoardResponseDTO createQuestion(QnaRequestDTO qnaForm, MultipartFile file) {
        User user = getUser(qnaForm.username());
        Book book = getBook(qnaForm.bookId());
        TeamSpace teamSpace = getTeamspace(qnaForm.teamSpaceId());

        BookQnaBoard newBoard = qnaForm.to(user, book, teamSpace);
        BookQnaBoard savedQuestion = questionRepository.save(newBoard);

        if (file != null && !file.isEmpty()) {
            s3FileService.uploadFile(file, savedQuestion.getId(), TargetType.QNA, String.valueOf(qnaForm.username()));
        }

        return QnaBoardResponseDTO.from(savedQuestion);
    }

    public Page<QnaBoardResponseDTO> getQuestionsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByTitleContaining(title, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    public Page<QnaBoardResponseDTO> getQuestionsByWriter(String username, int page, int size) {
        User user = getUser(username);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByWriter(user, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    @Transactional
    public QnaBoardResponseDTO updateQuestion(Long id, QnaRequestDTO qnaForm) {
        BookQnaBoard existBoard = questionRepository.findById(id)
                .orElseThrow(QuestionNotFoundException::new);

        if (!existBoard.getWriter().getUsername().equals(qnaForm.username())) {
            throw new UserUsernameException();
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

        return QnaBoardResponseDTO.from(questionRepository.save(updatedBoard));
    }

    @Transactional
    public void deleteQuestion(Long questionId, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(UserNotFoundException::new);

        BookQnaBoard question = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        if (!question.getWriter().getUsername().equals(user.getUsername())) {
            throw new UserUsernameException();
        }

        questionRepository.delete(question);
    }
}
