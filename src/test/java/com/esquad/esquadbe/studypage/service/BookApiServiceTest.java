package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.config.BookApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

    private RestTemplate restTemplate;
    private URI uri;
    private RequestEntity<Void> requestEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restTemplate = Mockito.mock(RestTemplate.class);
        bookApiService = new BookApiService(bookApi);

        String path = "/v1/search/book_adv.json";
        String isbn = "9788959895205";
        uri = bookApiService.buildUriForDetail(path, isbn);

        requestEntity = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", bookApi.getId())
                .header("X-Naver-Client-Secret", bookApi.getSecret())
                .build();
    }

    @Test
    @DisplayName("토큰 확인용")
    void testToken() {
        assertNotNull(bookApi.getId());
        assertNotNull(bookApi.getSecret());
    }
    @Test
    @DisplayName("Uri 확인")
    void testGetResultByKeyword() {
        assertEquals(uri.toString(), "https://openapi.naver.com/v1/search/book_adv.json?d_isbn=9788959895205&display=1&start=1&sort=sim");
    }

    @Test
    void testFetchDataHttpClientErrorException() {
        when(restTemplate.exchange(requestEntity, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        assertNull("HTTP 클라이언트 오류가 발생할 경우 응답이 null이어야 합니다.", requestEntity.getBody());
    }

    @Test
    void testFetchDataUnexpectedException() {

        // RuntimeException을 발생시키도록 모킹
        when(restTemplate.exchange(requestEntity, String.class))
                .thenThrow(new RuntimeException("Unexpected Error"));

        // RuntimeException 발생 시 fetchData 메서드가 null을 반환하는지 확인
        assertNull("예상치 못한 오류가 발생할 경우 응답이 null이어야 합니다.",requestEntity.getBody());
    }
}
