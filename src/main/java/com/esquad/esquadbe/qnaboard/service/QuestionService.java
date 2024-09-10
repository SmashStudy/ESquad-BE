package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final BookRepository bookRepository;
    private final TeamSpaceRepository teamSpaceRepository;

    // User, Book, TeamSpace 엔티티를 찾기 위한 헬퍼 메서드 추가
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
    }

    private TeamSpace findTeamSpaceById(Long teamSpaceId) {
        return teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamSpace not found with id: " + teamSpaceId));
    }

    // 전체 게시글 조회
    public Page<QnaBoardResponseDTO> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // questionRepository.findAll은 항상 빈 페이지를 반환하므로 null 검사 불필요
        Page<BookQnaBoard> questionPage = questionRepository.findAll(pageable);

        // 결과를 DTO로 매핑하여 반환
        return questionPage.map(QnaBoardResponseDTO::from);
    }


    // 특정 ID의 게시글 조회
    public QnaBoardResponseDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(QnaBoardResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
    }

    // 게시글 생성
    public QnaBoardResponseDTO createQuestion(String title, String content, Long userId, Long bookId, Long teamSpaceId) {
        // 헬퍼 메서드 사용
        User user = findUserById(userId);
        Book book = findBookById(bookId);
        TeamSpace teamSpace = findTeamSpaceById(teamSpaceId);

        BookQnaBoard question = BookQnaBoard.builder()
                .title(title)
                .content(content)
                .writer(user)
                .book(book)
                .teamSpace(teamSpace)
                .likes(0)
                .build();

        BookQnaBoard savedQuestion = questionRepository.save(question);
        return QnaBoardResponseDTO.from(savedQuestion);
    }

    // 특정 제목의 게시글 조회
    public Page<QnaBoardResponseDTO> getQuestionsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByTitleContaining(title, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    // 특정 작성자의 게시글 조회
    public Page<QnaBoardResponseDTO> getQuestionsByWriter(Long userId, int page, int size) {
        User user = findUserById(userId);  // 헬퍼 메서드 사용

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByWriter(user, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    // 게시글 수정 로직
    public QnaBoardResponseDTO updateQuestion(Long id, Long userId, String title, String content, Long bookId) {
        BookQnaBoard existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다: " + id));

        User user = findUserById(userId);    // 헬퍼 메서드 사용
        Book book = findBookById(bookId);

        existingQuestion.setTitle(title);
        existingQuestion.setContent(content);
        existingQuestion.setWriter(user);
        existingQuestion.setBook(book);

        BookQnaBoard updatedQuestion = questionRepository.save(existingQuestion);
        return QnaBoardResponseDTO.from(updatedQuestion);
    }

    // 게시글 삭제
    public void deleteQuestion(Long id) {
        BookQnaBoard question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
        questionRepository.delete(question);
    }
}
