package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultListDto;
import com.esquad.esquadbe.studypage.exception.BookMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BookMappingServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookMappingService bookMappingService;

    private final String addAttributeRes = "some invalid json string";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("JsonProcessingException 처리")
    void testMapToBookListWithJsonProcessingException() throws JsonProcessingException {

        when(objectMapper.readValue(addAttributeRes, BookSearchResultListDto.class))
                .thenThrow(new BookMappingException());

        assertThrows(BookMappingException.class, () -> bookMappingService.mapToBookList(addAttributeRes));
    }
}