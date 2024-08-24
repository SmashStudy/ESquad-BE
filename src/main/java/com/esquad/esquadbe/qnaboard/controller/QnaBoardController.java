package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardRequestsDTO;
import com.esquad.esquadbe.qnaboard.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QnaBoardController {

    private final QuestionService questionService;

    // 페이징된 전체 게시글 조회
    @GetMapping("/qna/questions")
    public Page<QnaBoardRequestsDTO> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size) {
        return questionService.getAllQuestions(page, size);
    }

    // 특정 ID의 게시글 조회
    @GetMapping("/qna/questions/{id}")
    public QnaBoardRequestsDTO getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }
}