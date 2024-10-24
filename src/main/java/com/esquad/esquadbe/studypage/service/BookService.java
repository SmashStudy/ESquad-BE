package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class BookService {
    private final BookUriService bookUriService;
    private final BookMappingService bookMappingService;
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookMappingService bookMappingService, BookUriService bookUriService, BookRepository bookRepository) {
        this.bookMappingService = bookMappingService;
        this.bookUriService = bookUriService;
        this.bookRepository = bookRepository;
    }

    public List<BookSearchResultItemDto> resultList(String query) {

        URI uri = BookUriService.buildUriForSearch("/v1/search/book.json", query);
        String res = bookUriService.fetchData(uri);

        return bookMappingService.mapToBookList(res);
    }

    public Long createBookInfo(@Valid BookSearchResultItemDto bookDto) {

        Book book = bookDto.to();
        Book savedBook = bookRepository.save(book);

        return savedBook.getId();
    }
}
