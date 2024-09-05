package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BookMappingServiceTest {
    private final String addAttributeRes = "{\"items\":[ {\n" +
            "        \"title\": \"네이버는 어떻게 일하는가 (네이버 그린팩토리는 24시간 멈추지 않는다)\",\n" +
            "        \"link\": \"https://search.shopping.naver.com/book/catalog/32455473672\",\n" +
            "        \"image\": \"https://shopping-phinf.pstatic.net/main_3245547/32455473672.20220527083840.jpg\",\n" +
            "        \"author\": \"신무경\",\n" +
            "        \"discount\": \"0\",\n" +
            "        \"publisher\": \"미래의창\",\n" +
            "        \"isbn\": \"9788959895205\",\n" +
            "        \"description\": \"누구도 멈출 수 없는 포털의 지배자가 되기까지...\",  \n" +
            "        \"pubdate\": \"20180608\"\n" +
            "        \"new\": true\n" +
            "    } ]}";
    @InjectMocks
    private BookMappingService bookMappingService;
    @Mock
    private ObjectMapper objectMapper;
    private JsonProcessingException exception;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exception = new JsonProcessingException("Json Processing error") {
        };
    }

    @Test
    @DisplayName("JsonProcessingException 처리")
    void testMapToBookListWithJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.readValue(addAttributeRes, BookSearchResultItemDto.class)).thenThrow(exception);

        List<BookSearchResultItemDto> result = bookMappingService.mapToBookList(addAttributeRes);

        assertTrue(result.isEmpty(), "JSON 처리 오류가 발생하면 빈 리스트를 반환해야 합니다.");
    }
}