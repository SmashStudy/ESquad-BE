package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.vo.BookDetailVo;
import com.esquad.esquadbe.studypage.vo.BookVo;
import com.esquad.esquadbe.studypage.vo.BookDetailResultVo;
import com.esquad.esquadbe.studypage.vo.BookResultVo;
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

    public List<BookVo> mapToBookList(String jsonResponse) {
        if (jsonResponse == null) {
            log.warn("JSON 응답이 null입니다. 빈 리스트를 반환합니다.");
            return Collections.emptyList();
        }

        try {
            BookResultVo bookResultVo = objectMapper.readValue(jsonResponse, BookResultVo.class);
            return bookResultVo.getItems();
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", e);
            return Collections.emptyList();
        }
    }
    public List<BookDetailVo> mapToBook(String jsonResponse) {
        if (jsonResponse == null) {
            log.warn("JSON 응답이 null입니다. 빈 리스트를 반환합니다.");
            return Collections.emptyList();
        }

        try {
            BookDetailResultVo bookDetailResultVo = objectMapper.readValue(jsonResponse, BookDetailResultVo.class);
            return bookDetailResultVo.getItems();
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", e);
            return Collections.emptyList();
        }
    }
}