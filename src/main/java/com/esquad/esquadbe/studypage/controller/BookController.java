package com.esquad.esquadbe.studypage.controller;

import com.esquad.esquadbe.studypage.service.BookService;
import com.esquad.esquadbe.studypage.dto.BookSearchResultItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookSearchResultItemDto>> searchTitle(@RequestParam("query") String query) {

        List<BookSearchResultItemDto> bookList = bookService.resultList(query);
        return ResponseEntity.status(200).body(bookList);
    }
}
