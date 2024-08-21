package com.esquad.esquadbe.controller;

import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class QnaBoardController {

    private final QuestionRepository questionRepository;

    @GetMapping("/qna/questions")
    public List<BookQnaBoard> getAllQuestions() {
        List<BookQnaBoard> bookQnaBoards = new ArrayList<>();
        for(BookQnaBoard question : questionRepository.findAll()) {
            bookQnaBoards.add(question);
        }

        return bookQnaBoards;
    }
}
