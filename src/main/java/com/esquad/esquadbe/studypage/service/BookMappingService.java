package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.vo.BookDetailVo;
import com.esquad.esquadbe.studypage.vo.BookVo;
import com.esquad.esquadbe.studypage.vo.ResultDetailVo;
import com.esquad.esquadbe.studypage.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
            ResultVo resultVo = objectMapper.readValue(jsonResponse, ResultVo.class);
            return resultVo.getItems();
        } catch (JsonMappingException e) {
            log.error("JSON 매핑 오류: 응답을 ResultVo 객체로 변환하는 중 문제가 발생했습니다.", e);
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: JSON 응답을 처리하는 중 문제가 발생했습니다.", e);
        }

        return Collections.emptyList();
    }
    public List<BookDetailVo> mapToBook(String jsonResponse) {
        if (jsonResponse == null) {
            log.warn("JSON 응답이 null입니다. 빈 리스트를 반환합니다.");
            return Collections.emptyList();
        }

        try {
            ResultDetailVo resultDetailVo = objectMapper.readValue(jsonResponse, ResultDetailVo.class);
            return resultDetailVo.getItems();
        } catch (JsonMappingException e) {
            log.error("JSON 매핑 오류: 응답을 ResultVo 객체로 변환하는 중 문제가 발생했습니다.", e);
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: 응답 처리 중 문제가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: JSON 응답을 처리하는 중 문제가 발생했습니다.", e);
        }

        return Collections.emptyList();
    }
}