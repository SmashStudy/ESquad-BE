package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.vo.BookVo;
import com.esquad.esquadbe.studypage.vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BookMappingServiceTest {
    @InjectMocks
    private BookMappingService bookMappingService;

    @Mock
    private ObjectMapper objectMapper;

    private final String jsonResponse = "{ \"items\": [ {\n" +
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
    private ResultVo expectedResultVo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expectedResultVo = new ResultVo();
        expectedResultVo.setItems(Collections.singletonList(
                new BookVo("네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)",
                        "https://search.shopping.naver.com/book/catalog/32455473672",
                        "https://shopping-phinf.pstatic.net/main_3245547/32455473672.20220527083840.jpg",
                        "신무경", "미래의창", "9788959895205", "누구도 멈출 수 없는 포털의 지배자가 되기까지...",
                        "20180608", "0")
        ));
    }

    @Test
    @DisplayName("Null 응답 시 빈 리스트 반환")
    void testMapToBookListWithNullResponse() {
        List<BookVo> result = bookMappingService.mapToBookList(null);
        assertTrue(result.isEmpty(), "JSON 응답이 null일 때 빈 리스트를 반환해야 합니다.");
    }

    @Test
    @DisplayName("유효한 JSON 응답 처리")
    void testMapToBookListWithValidResponse() throws Exception {
        when(objectMapper.readValue(jsonResponse, ResultVo.class)).thenReturn(expectedResultVo);

        List<BookVo> result = bookMappingService.mapToBookList(jsonResponse);
        assertEquals(1, result.size(), "책 리스트의 크기가 1이어야 합니다.");
        assertEquals(expectedResultVo.getItems().get(0).getTitle(), result.get(0).getTitle(), "책 제목이 예상과 일치해야 합니다.");
    }

    @Test
    @DisplayName("JsonProcessingException 처리")
    void testMapToBookListWithJsonProcessingException() throws Exception {
        testExceptionHandling(new JsonProcessingException("Processing error") {});
    }

    private void testExceptionHandling(JsonProcessingException exception) throws Exception {
        when(objectMapper.readValue(jsonResponse, ResultVo.class)).thenThrow(exception);

        List<BookVo> result = bookMappingService.mapToBookList(jsonResponse);
        assertTrue(result.isEmpty(), "JSON 처리 오류가 발생하면 빈 리스트를 반환해야 합니다.");
    }
}
