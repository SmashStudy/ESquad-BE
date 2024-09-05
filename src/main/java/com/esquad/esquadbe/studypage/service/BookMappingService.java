package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import com.esquad.esquadbe.studypage.dto.BookSearchResultListDto;
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

    public List<BookSearchResultItemDto> mapToBookList(String res) {
        if(res == null || res.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            BookSearchResultListDto dto = objectMapper.readValue(res, BookSearchResultListDto.class);
            return dto.getItems();
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", e);
            return Collections.emptyList();
        }
    }
}