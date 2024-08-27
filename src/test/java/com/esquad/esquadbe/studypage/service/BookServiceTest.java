package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchDetailDto;
import com.esquad.esquadbe.studypage.dto.BookSearchDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class BookServiceTest {
    @Mock
    private BookApiService bookApiService;

    @Mock
    private BookMappingService bookMappingService;

    @InjectMocks
    private BookService bookService;

    String jsonResponse = "{ \"items\": [ {\n" +
            "        \"title\": \"네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)\",\n" +
            "        \"link\": \"https://search.shopping.naver.com/book/catalog/32455473672\",\n" +
            "        \"image\": \"https://shopping-phinf.pstatic.net/main_3245547/32455473672.20220527083840.jpg\",\n" +
            "        \"author\": \"신무경\",\n" +
            "        \"discount\": \"0\",\n" +
            "        \"publisher\": \"미래의창\",\n" +
            "        \"isbn\": \"9788959895205\",\n" +
            "        \"description\": \"누구도 멈출 수 없는 포털의 지배자가 되기까지...\",  \n" +
            "        \"pubdate\": \"20180608\"\n" +
            "    } ] }";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유효한 검색어에 대한 조회 시 도서 목록 반환")
    void testResultListSuccess() {

        URI uri = BookApiService.buildUriForSearch("/v1/search/book.json", "그린팩토리", 100);

        when(bookApiService.fetchData(uri)).thenReturn(jsonResponse);

        BookSearchDto expectedBook = new BookSearchDto(
                "네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)",
                "https://search.shopping.naver.com/book/catalog/32455473672",
                "https://shopping-phinf.pstatic.net/main_3245547/32455473672.20220527083840.jpg",
                "신무경", "미래의창", "9788959895205",
                "누구도 멈출 수 없는 포털의 지배자가 되기까지...",
                "20180608", "0"
        );
        when(bookMappingService.mapToBookList(jsonResponse)).thenReturn(Collections.singletonList(expectedBook));

        List<BookSearchDto> result = bookService.resultList("그린팩토리");

        assertEquals(expectedBook.getTitle(), result.get(0).getTitle(), "책 제목이 예상과 일치해야 합니다.");
        assertNotNull(result, "책 리스트는 null이 아니어야 합니다.");
        assertEquals(1, result.size(), "책 리스트의 크기가 1이어야 합니다.");
    }

    @Test
    @DisplayName("유효하지 않은 검색어에 대한 조회 시 빈 목록 반환")
    void testResultListFetchDataReturnsNull() {
        URI uri = URI.create("https://api.example.com/books?query=그린팩토리");
        when(bookApiService.fetchData(uri)).thenReturn(null);

        List<BookSearchDto> result = bookService.resultList("그린팩토리");

        assertNotNull(result, "책 리스트는 null이 아니어야 합니다.");
        assertTrue(result.isEmpty(), "책 리스트는 비어 있어야 합니다.");
    }

    @Test
    @DisplayName("유효한 isdn에 대한 조회 시 도서 상세 정보 반환")
    void testResultDetailSuccess() {
        URI uri = BookApiService.buildUriForDetail("/v1/search/book_adv.json", "9788959895205");
        when(bookApiService.fetchData(uri)).thenReturn(jsonResponse);
        BookSearchDetailDto expectedBook = new BookSearchDetailDto(
                "네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)",
                "https://search.shopping.naver.com/book/catalog/32455473672",
                "https://shopping-phinf.pstatic.net/main_3245547/32455473672.20220527083840.jpg",
                "신무경", "미래의창", "9788959895205",
                "누구도 멈출 수 없는 포털의 지배자가 되기까지...",
                "20180608"
        );
        when(bookMappingService.mapToBook(jsonResponse)).thenReturn(Collections.singletonList(expectedBook));

        List<BookSearchDetailDto> result = bookService.resultDetail("9788959895205");

        assertNotNull(result, "책 리스트는 null이 아니어야 합니다.");
        assertEquals(1, result.size(), "책 리스트의 크기가 1이어야 합니다.");
        assertEquals("네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)", result.get(0).getTitle(), "책 제목이 '네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)'이어야 합니다.");
        assertEquals("9788959895205", result.get(0).getIsbn(), "ISBN이 '9788959895205'이어야 합니다.");
    }

    @Test
    @DisplayName("유효하지 않은 isbn에 대한 조회 시 빈 목록 반환")
    void testResultDetailFetchDataReturnsNull() {
        URI uri = URI.create("https://api.example.com/books?isbn=1234567890");
        when(bookApiService.fetchData(uri)).thenReturn(null);

        List<BookSearchDetailDto> result = bookService.resultDetail("1234567890");

        assertNotNull(result, "책 리스트는 null이 아니어야 합니다.");
        assertTrue(result.isEmpty(), "책 리스트는 비어 있어야 합니다.");
    }
}