package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class BookService {
    //
    private final BookUriService bookUriService;
    private final BookMappingService bookMappingService;

    @Autowired
    public BookService(BookMappingService bookMappingService, BookUriService bookUriService) {
        this.bookMappingService = bookMappingService;
        this.bookUriService = bookUriService;
    }

    public List<BookSearchResultItemDto> resultList(String query) {
        //요청용 uri 제작
        URI uri = BookUriService.buildUriForSearch("/v1/search/book.json", query);
        //요청 uri 를 받아와 api 토큰을 합쳐 api 요청을 보내 string 형식으로 반환한다.
        String res = bookUriService.fetchData(uri);
        //책 목록만 가져오도록 변경
        return bookMappingService.mapToBookList(res);
    }
}
