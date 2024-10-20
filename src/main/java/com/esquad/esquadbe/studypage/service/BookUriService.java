package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.config.BookApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
public class BookUriService {

    private final BookApi bookApi;
    private final RestTemplate restTemplate;

    @Autowired
    public BookUriService(BookApi bookApi) {
        this.bookApi = bookApi;
        this.restTemplate = new RestTemplate();
    }

    public static URI buildUriForSearch(String path, String query) {
        return UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path(path)
                .queryParam("query", query)
                .queryParam("display", 100)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();
    }

    public String fetchData(URI uri) {
        RequestEntity<Void> req = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", bookApi.getId())
                .header("X-Naver-Client-Secret", bookApi.getSecret())
                .build();
        try {
            return restTemplate.exchange(req, String.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException 발생: 상태 코드: {}, 응답 본문: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("네이버 API 요청 실패: " + e.getMessage(), e);
        }
    }
}