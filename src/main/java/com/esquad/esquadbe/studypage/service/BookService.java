package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchDetailDto;
import com.esquad.esquadbe.studypage.dto.BookSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class BookService {
    private final BookApiService bookApiService;
    private final BookMappingService bookMappingService;

    @Autowired
    public BookService(BookMappingService bookMappingService, BookApiService bookApiService) {
        this.bookMappingService = bookMappingService;
        this.bookApiService = bookApiService;
    }

    public List<BookSearchDto> resultList(String text) {
        URI uri = BookApiService.buildUriForSearch("/v1/search/book.json", text, 100);
        String response = bookApiService.fetchData(uri);
        return bookMappingService.mapToBookList(response);
    }

    public List<BookSearchDetailDto> resultDetail(String isbn) {
        URI uri = BookApiService.buildUriForDetail("/v1/search/book_adv.json", isbn);
        String response = bookApiService.fetchData(uri);
        return bookMappingService.mapToBook(response);
    }
}
