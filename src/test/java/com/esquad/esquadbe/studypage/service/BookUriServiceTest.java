package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.config.BookApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookUriServiceTest {

    @Autowired
    private BookApi bookApi;
    private URI uri;

    @BeforeEach
    void setUp() {
        final String path = "/v1/search/book.json";
        final String isbn = "9788959895205";
        uri = BookUriService.buildUriForSearch(path, isbn);

    }

    @Test
    @DisplayName("Token 확인")
    void testToken() {
        RestTemplate restTemplate = new RestTemplate();
        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> {
                    RequestEntity<Void> req = RequestEntity.get(uri)
                            .header("X-Naver-Client-Id", bookApi.getId()+"ds")
                            .header("X-Naver-Client-Secret", bookApi.getSecret())
                            .build();
                    restTemplate.exchange(req, String.class).getBody();
                });

        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("Unauthorized"));
    }

    @Test
    @DisplayName("Uri 확인")
    void testGetResultByKeyword() {
        String actualUri = uri.toString();

        assertEquals("https://openapi.naver.com/v1/search/book_adv.json?d_isbn=9788959895205&display=1&start=1&sort=sim", actualUri);
    }
}