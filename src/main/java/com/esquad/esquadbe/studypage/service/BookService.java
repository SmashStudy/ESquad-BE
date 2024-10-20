package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Slf4j
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

    public Long createBookInfo(BookSearchResultItemDto bookDto) {
        //책 정보
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .imgPath(bookDto.getImage())
                .author(bookDto.getAuthor())
                .publisher(bookDto.getPublisher())
                .isbn(bookDto.getIsbn())
                .description(bookDto.getDescription())
                .pubDate(bookDto.getPubdate())
                .build();

        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }
}
