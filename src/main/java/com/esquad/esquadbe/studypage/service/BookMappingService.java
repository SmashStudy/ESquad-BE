package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import com.esquad.esquadbe.studypage.dto.BookSearchResultListDto;
import com.esquad.esquadbe.studypage.exception.BookMappingException;
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
            return objectMapper.readValue(res, BookSearchResultListDto.class).getItems();
        } catch (JsonProcessingException e) {
            throw new BookMappingException();
        }
    }
}