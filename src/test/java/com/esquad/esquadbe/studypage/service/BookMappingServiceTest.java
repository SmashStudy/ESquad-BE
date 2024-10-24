package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import com.esquad.esquadbe.studypage.dto.BookSearchResultListDto;
import com.esquad.esquadbe.studypage.exception.BookMappingException;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BookMappingServiceTest {

    @Mock
    private ObjectMapper objectMapper; // 목 객체 설정

    @InjectMocks
    private BookMappingService bookMappingService; // 목 객체가 주입된 서비스

    private String addAttributeRes = "some invalid json string"; // 테스트용 데이터

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    @DisplayName("JsonProcessingException 처리")
    void testMapToBookListWithJsonProcessingException() throws JsonProcessingException {
        // Given: ObjectMapper에서 JsonProcessingException이 발생하도록 설정
        when(objectMapper.readValue(addAttributeRes, BookSearchResultListDto.class))
                .thenThrow(new BookMappingException());

        // When & Then: BookMappingException 예외가 발생해야 함
        assertThrows(BookMappingException.class, () -> {
            bookMappingService.mapToBookList(addAttributeRes);
        });
    }
}