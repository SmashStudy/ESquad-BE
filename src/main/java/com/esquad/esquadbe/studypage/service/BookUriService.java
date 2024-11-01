package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.config.BookApi;
import com.esquad.esquadbe.studypage.exception.BookUriException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
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
        try {
            RequestEntity<Void> req = RequestEntity.get(uri)
                    .header("X-Naver-Client-Id", bookApi.getId())
                    .header("X-Naver-Client-Secret", bookApi.getSecret())
                    .build();

            return restTemplate.exchange(req, String.class).getBody();
        }catch (HttpClientErrorException e) {
            throw new BookUriException();
        }
    }
}