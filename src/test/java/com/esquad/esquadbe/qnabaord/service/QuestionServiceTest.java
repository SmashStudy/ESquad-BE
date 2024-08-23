package com.esquad.esquadbe.qnabaord.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardRequestsDto;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.qnaboard.service.QuestionService;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private BookQnaBoard bookQnaBoard;

    @BeforeEach
    void setUp() {
        // User 객체 생성
        User writer = User.builder()
                .id(1L)
                .userId("wnsgud0310")
                .nickname("박준형")
                .password("wnsgud0310")
                .userName("Test User")
                .email("ffjjo0310@gmail.com")
                .phoneNo("01094987919")
                .birthDay(LocalDate.of(2000, 3, 10))
                .address("부산해운대구좌동")
                .build();

        // StudyPage 및 Book 객체 생성
        StudyPage studyPage = StudyPage.builder()
                .id(1L)
                .description("Study Page")
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("실전스프링부트")
                .build();

        // BookQnaBoard 객체 생성
        bookQnaBoard = BookQnaBoard.builder()
                .id(1L)
                .writer(writer)
                .studyPage(studyPage)
                .book(book)
                .title("실전스프링부트")
                .content("스프링부트에관하여")
                .likes(5)
                .build();
    }

    @Test
    void testGetAllQuestions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookQnaBoard> page = new PageImpl<>(Arrays.asList(bookQnaBoard), pageable, 1);

        when(questionRepository.findAll(pageable)).thenReturn(page);

        Page<QnaBoardRequestsDto> dtos = questionService.getAllQuestions(0, 10);

        assertNotNull(dtos);
        assertEquals(1, dtos.getTotalElements());
        assertEquals("실전스프링부트", dtos.getContent().get(0).getTitle());

        verify(questionRepository, times(1)).findAll(pageable);
    }


    @Test
    void testGetQuestionById() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(bookQnaBoard));

        QnaBoardRequestsDto dto = questionService.getQuestionById(1L);

        assertNotNull(dto);
        assertEquals("실전스프링부트", dto.getTitle());

        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetQuestionById_NotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionById(1L));

        verify(questionRepository, times(1)).findById(1L);
    }
}
