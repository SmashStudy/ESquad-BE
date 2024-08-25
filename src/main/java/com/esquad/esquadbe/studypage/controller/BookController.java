package com.esquad.esquadbe.studypage.controller;

import com.esquad.esquadbe.studypage.service.BookService;
import com.esquad.esquadbe.studypage.vo.BookDetailVo;
import com.esquad.esquadbe.studypage.vo.BookVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookVo>> searchTitle(@RequestParam("title") String title) {
        log.info("받은 제목: {}", title);

        List<BookVo> bookList = bookService.resultList(title);

        return ResponseEntity.status(200).body(bookList);
    }

    @GetMapping("/search/{isbn}")
    public ResponseEntity<List<BookDetailVo>> showDetail(@PathVariable String isbn) {
        log.info("받은 ISBN: {}", isbn);

        List<BookDetailVo> book = bookService.resultDetail(isbn);

        return ResponseEntity.status(200).body(book);
    }
}
