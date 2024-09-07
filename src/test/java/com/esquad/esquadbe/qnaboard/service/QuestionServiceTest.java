package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private BookQnaBoard sampleQuestion;

    @BeforeEach
    void setUp() {
        sampleQuestion = BookQnaBoard.builder()
                .id(1L)
                .title("샘플 제목")
                .content("샘플 내용")
                .writer("샘플 작성자")
                .book("샘플 책")
                .likes(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetQuestionById_Success() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        QnaBoardResponseDTO responseDTO = questionService.getQuestionById(1L);

        assertNotNull(responseDTO, "응답이 null이 아니어야 합니다");
        assertEquals("샘플 제목", responseDTO.getTitle(), "제목이 샘플 제목과 일치해야 합니다");
        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetQuestionById_NotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionService.getQuestionById(1L);
        });

        assertEquals("해당 게시물을 찾을 수 없습니다: 1", exception.getMessage());
        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateQuestion() {
        when(questionRepository.save(any(BookQnaBoard.class))).thenReturn(sampleQuestion);

        QnaBoardResponseDTO responseDTO = questionService.createQuestion("샘플 제목", "샘플 내용", "샘플 작성자", "샘플 책");

        assertNotNull(responseDTO, "응답이 null이 아니어야 합니다");
        assertEquals("샘플 제목", responseDTO.getTitle(), "생성된 질문의 제목이 샘플 제목과 일치해야 합니다");
        verify(questionRepository, times(1)).save(any(BookQnaBoard.class));
    }

    @Test
    void testUpdateQuestion() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));
        when(questionRepository.save(any(BookQnaBoard.class))).thenReturn(sampleQuestion);

        QnaBoardResponseDTO responseDTO = questionService.updateQuestion(1L, "수정된 제목", "수정된 내용", "수정된 책");

        assertNotNull(responseDTO, "응답이 null이 아니어야 합니다");
        assertEquals("수정된 제목", responseDTO.getTitle(), "수정된 질문의 제목이 수정된 제목과 일치해야 합니다");
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).save(any(BookQnaBoard.class));
    }

    @Test
    void testDeleteQuestion_Success() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        questionService.deleteQuestion(1L);

        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).delete(sampleQuestion);
    }

    @Test
    void testDeleteQuestion_NotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionService.deleteQuestion(1L);
        });

        assertEquals("해당 게시물을 찾을 수 없습니다: 1", exception.getMessage());
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(0)).delete(any(BookQnaBoard.class));
    }

    @Test
    void testGetAllQuestions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookQnaBoard> bookQnaBoardPage = new PageImpl<>(Collections.singletonList(sampleQuestion));

        when(questionRepository.findAll(pageable)).thenReturn(bookQnaBoardPage);

        Page<QnaBoardResponseDTO> questions = questionService.getAllQuestions(0, 10);

        assertNotNull(questions, "반환된 페이지는 null이 아니어야 합니다");
        assertEquals(0, questions.getTotalElements(), "총 요소 수는 0이어야 합니다");
        if (!questions.getContent().isEmpty()) {
            assertEquals("샘플 제목", questions.getContent().get(0).getTitle(), "제목이 샘플 제목과 일치해야 합니다");
        } else {
            fail("질문 목록이 비어 있지 않아야 합니다");
        }

        verify(questionRepository, times(1)).findAll(pageable);
    }
}
