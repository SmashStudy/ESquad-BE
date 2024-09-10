package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TeamSpaceRepository teamSpaceRepository;

    @InjectMocks
    private QuestionService questionService;

    private User user;
    private Book book;
    private TeamSpace teamSpace;
    private BookQnaBoard question;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 가짜 엔티티들 생성
        user = new User();  // User 객체 초기화 확인
        book = new Book();  // Book 객체 초기화 확인
        teamSpace = new TeamSpace();  // TeamSpace 객체 초기화 확인

        // 가짜 데이터 설정
        question = BookQnaBoard.builder()
                .title("Test Title")
                .content("Test Content")
                .writer(user)
                .book(book)
                .teamSpace(teamSpace)
                .likes(0)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
    @Test
    void 질문_생성_성공_저장된_질문_반환() {
        // Mock 리포지토리 동작 정의
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(teamSpaceRepository.findById(1L)).thenReturn(Optional.of(teamSpace));
        when(questionRepository.save(any(BookQnaBoard.class))).thenReturn(question);

        // 테스트 수행
        QnaBoardResponseDTO result = questionService.createQuestion("Test Title", "Test Content", 1L, 1L, 1L);

        // 검증
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        verify(questionRepository, times(1)).save(any(BookQnaBoard.class));
    }

    @Test
    void 유저가_없을_때_예외_발생() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // 유저가 없을 때 예외 발생 확인
        assertThrows(RuntimeException.class, () -> questionService.createQuestion("Test Title", "Test Content", 1L, 1L, 1L));
    }

    @Test
    void 도서가_없을때_예외_발생() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // 도서가 없을 때 예외 발생 확인
        assertThrows(RuntimeException.class, () -> questionService.createQuestion("Test Title", "Test Content", 1L, 1L, 1L));
    }

    @Test
    void 팀스페이스가_없을때_예외_발생() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(teamSpaceRepository.findById(1L)).thenReturn(Optional.empty());

        // 팀 스페이스가 없을 때 예외 발생 확인
        assertThrows(RuntimeException.class, () -> questionService.createQuestion("Test Title", "Test Content", 1L, 1L, 1L));
    }
    @Test
    void 게시글_수정_성공_수정된_게시글_반환() {
        // Mock 리포지토리 동작 정의
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(questionRepository.save(any(BookQnaBoard.class))).thenReturn(question);

        // 업데이트를 시도할 새 데이터
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        // 서비스 메서드 호출
        QnaBoardResponseDTO result = questionService.updateQuestion(1L, 1L, updatedTitle, updatedContent, 1L);

        // 검증
        assertNotNull(result);
        assertEquals(updatedTitle, result.getTitle());
        assertEquals(updatedContent, result.getContent());
        verify(questionRepository, times(1)).save(any(BookQnaBoard.class));
    }

    @Test
    void 게시글_없을때_수정_예외_발생() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                questionService.updateQuestion(1L, 1L, "Updated Title", "Updated Content", 1L));

        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, never()).save(any(BookQnaBoard.class));
    }

    @Test
    void 유저가_없을때_수정_예외_발생() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                questionService.updateQuestion(1L, 1L, "Updated Title", "Updated Content", 1L));

        verify(userRepository, times(1)).findById(1L);
        verify(questionRepository, never()).save(any(BookQnaBoard.class));
    }

    @Test
    void 책이_없을때_수정_예외_발생() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                questionService.updateQuestion(1L, 1L, "Updated Title", "Updated Content", 1L));

        verify(bookRepository, times(1)).findById(1L);
        verify(questionRepository, never()).save(any(BookQnaBoard.class));
    }
    @Test
    void 게시글_삭제_성공() {
        // Mock 리포지토리 동작 정의: 삭제할 게시글이 존재할 때
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // 서비스 메서드 호출
        questionService.deleteQuestion(1L);

        // 검증: findById와 delete 메서드가 호출되었는지 확인
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void 게시글_없을떄_삭제_예외_발생() {
        // Mock 리포지토리 동작 정의: 삭제할 게시글이 존재하지 않을 때
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // 서비스 메서드가 예외를 던지는지 확인
        assertThrows(RuntimeException.class, () -> questionService.deleteQuestion(1L));

        // 검증: findById는 호출되었으나 delete는 호출되지 않았는지 확인
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, never()).delete(any(BookQnaBoard.class));
    }
    @Test
    void 게시글_생성_후_조회_성공() {
        // Mock 리포지토리 동작 정의: 유저, 책, 팀 스페이스를 찾을 수 있을 때
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(teamSpaceRepository.findById(1L)).thenReturn(Optional.of(teamSpace));

        // 게시글 생성 후, 해당 게시글이 저장되었다고 가정
        when(questionRepository.save(any(BookQnaBoard.class))).thenReturn(question);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // 서비스 메서드 호출하여 게시글 생성
        QnaBoardResponseDTO createdResult = questionService.createQuestion("Test Title", "Test Content", 1L, 1L, 1L);

        // 검증: 생성된 게시글이 정확한지 확인
        assertNotNull(createdResult);
        assertEquals("Test Title", createdResult.getTitle());
        assertEquals("Test Content", createdResult.getContent());
        verify(questionRepository, times(1)).save(any(BookQnaBoard.class));

        // 생성된 게시글을 조회하는 테스트 수행
        QnaBoardResponseDTO retrievedResult = questionService.getQuestionById(1L);

        // 검증: 조회된 게시글이 생성된 게시글과 일치하는지 확인
        assertNotNull(retrievedResult);
        assertEquals("Test Title", retrievedResult.getTitle());
        assertEquals("Test Content", retrievedResult.getContent());
        verify(questionRepository, times(1)).findById(1L);
    }
    @Test
    void 전체_게시글_조회_성공() {
        // Given: 페이지 요청을 위한 Pageable 설정
        Pageable pageable = PageRequest.of(0, 10);

        // Mock 리포지토리 동작 정의: 전체 게시글 목록을 페이징된 형태로 반환하도록 설정
        List<BookQnaBoard> questions = List.of(question);  // 미리 정의한 `question` 객체
        Page<BookQnaBoard> pagedQuestions = new PageImpl<>(questions, pageable, questions.size());

        // Mock 동작 정의: questionRepository.findAll이 페이징된 질문 목록을 반환하도록 설정
        when(questionRepository.findAll(any(Pageable.class))).thenReturn(pagedQuestions);

        // When: 서비스 메서드 호출
        Page<QnaBoardResponseDTO> result = questionService.getAllQuestions(0, 10);

        // Then: 반환된 결과가 예상과 일치하는지 검증
        assertNotNull(result);  // 결과가 null이 아님을 확인
        assertEquals(1, result.getTotalElements());  // 질문이 1개만 있다고 가정
        assertEquals("Test Title", result.getContent().get(0).getTitle());  // 제목이 일치하는지 확인
        verify(questionRepository, times(1)).findAll(any(Pageable.class));  // 리포지토리가 1번 호출되었는지 확인
    }


}


