package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.config.BookApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class BookApiServiceTest {

    @Autowired
    private BookApiService bookApiService;

    @Autowired
    private BookApi bookApi;

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private URI uri;
    private RequestEntity<Void> requestEntity;

    @BeforeEach
    void setUp() {
        final String path = "/v1/search/book_adv.json";
        final String isbn = "9788959895205";
        uri = BookApiService.buildUriForDetail(path, isbn);

        requestEntity = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", bookApi.getId())
                .header("X-Naver-Client-Secret", bookApi.getSecret())
                .build();
    }

    @Test
    @DisplayName("Token 확인")
    void testToken() {
        String clientId = bookApi.getId();
        String clientSecret = bookApi.getSecret();

        assertNotNull(clientId);
        assertNotNull(clientSecret);
    }
    @Test
    @DisplayName("Uri 확인")
    void testGetResultByKeyword() {
        String actualUri = uri.toString();

        assertEquals("https://openapi.naver.com/v1/search/book_adv.json?d_isbn=9788959895205&display=1&start=1&sort=sim", actualUri);
    }

    @Test
    @DisplayName("HttpClientErrorException 처리")
    void testFetchDataHttpClientErrorException() {
        when(restTemplate.exchange(requestEntity, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        String result = bookApiService.fetchData(uri);

        assertNull("HTTP 클라이언트 오류가 발생할 경우 응답이 null이어야 합니다.", result);
    }
}
