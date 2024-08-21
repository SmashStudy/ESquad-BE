package com.esquad.esquadbe.controller;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class QnaBoardController {

    private final QuestionRepository questionRepository;
    //전체 게시글 조회
    @GetMapping("/qna/questions")
    public List<BookQnaBoard> getAllQuestions() {
        List<BookQnaBoard> bookQnaBoards = new ArrayList<>();
        for(BookQnaBoard question : questionRepository.findAll()) {
            bookQnaBoards.add(question);
        }
        return bookQnaBoards;
    }
    @GetMapping("/qna/questions/{id}")
    public BookQnaBoard getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id를 찾을수가 없습니다. " + id));
    }

}
