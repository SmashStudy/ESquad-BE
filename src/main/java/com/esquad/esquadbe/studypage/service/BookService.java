package com.esquad.esquadbe.studypage.service;

import java.net.*;
import java.util.List;

import com.esquad.esquadbe.studypage.config.BookApi;
import com.esquad.esquadbe.studypage.vo.BookVo;
import com.esquad.esquadbe.studypage.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class BookService {

    private final BookApi bookApi;

    @Autowired
    public BookService(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    public List<BookVo> resultList(String text) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper om = new ObjectMapper();
        ResponseEntity<String> resp;
        ResultVo resultVo = null;

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", text)
                .queryParam("display", 100)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> req = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", bookApi.getId())
                .header("X-Naver-Client-Secret", bookApi.getSecret())
                .build();

        try {
            resp = restTemplate.exchange(req, String.class);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: 상태 코드: {}, 응답 본문: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return List.of();
        } catch (Exception e) {
            log.error("네이버 API 요청 처리 중 예상치 못한 오류 발생", e);
            return List.of();
        }

        try {
            resultVo = om.readValue(resp.getBody(), ResultVo.class);
        } catch (JsonMappingException e) {
            log.error("JSON 매핑 오류 발생: 응답 데이터를 ResultVo로 변환하는 중 문제가 발생했습니다.", e);
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류 발생: 응답 본문을 처리하는 중 문제가 발생했습니다.", e);
        }

        return resultVo != null ? resultVo.getItems() : List.of();
    }

    public List<BookVo> resultDetail(String isbn) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper om = new ObjectMapper();
        ResponseEntity<String> resp;
        ResultVo resultVo = null;

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book_adv.json")
                .queryParam("d_isbn", isbn)
                .queryParam("display", 1)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> req = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", bookApi.getId())
                .header("X-Naver-Client-Secret", bookApi.getSecret())
                .build();

        try {
            resp = restTemplate.exchange(req, String.class);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: 상태 코드: {}, 응답 본문: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return List.of();
        } catch (Exception e) {
            log.error("네이버 API 요청 처리 중 예상치 못한 오류 발생", e);
            return List.of();
        }

        try {
            resultVo = om.readValue(resp.getBody(), ResultVo.class);
        } catch (JsonMappingException e) {
            log.error("JSON 매핑 오류 발생: 응답 데이터를 ResultVo로 변환하는 중 문제가 발생했습니다.", e);
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류 발생: 응답 본문을 처리하는 중 문제가 발생했습니다.", e);
        }

        return resultVo != null ? resultVo.getItems() : List.of();
    }
}
