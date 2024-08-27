package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchDetailDto;
import com.esquad.esquadbe.studypage.dto.BookSearchDetailResultDto;
import com.esquad.esquadbe.studypage.dto.BookSearchDto;
import com.esquad.esquadbe.studypage.dto.BookSearchResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class BookMappingService {

    private final ObjectMapper objectMapper;

    public BookMappingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<BookSearchDto> mapToBookList(String jsonResponse) {
        if(jsonResponse == null || jsonResponse.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            BookSearchResultDto bookSearchResultDto = objectMapper.readValue(jsonResponse, BookSearchResultDto.class);
            return bookSearchResultDto.getItems();
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", e);
            return Collections.emptyList();
        }
    }

    public List<BookSearchDetailDto> mapToBook(String jsonResponse) {
        try {
            BookSearchDetailResultDto bookSearchDetailResultDto = objectMapper.readValue(jsonResponse, BookSearchDetailResultDto.class);
            return bookSearchDetailResultDto.getItems();
        } catch (JsonProcessingException jpe) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", jpe);
            return Collections.emptyList();
        }
    }
}