package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardRequestsDto;
import com.esquad.esquadbe.qnaboard.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class QnaBoardController {

    private final QuestionService questionService;

    // 게시글 전체 조회
    @GetMapping("/qna/questions")
    public List<QnaBoardRequestsDto> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // 특정 ID의 게시글 조회
    @GetMapping("/qna/questions/{id}")
    public QnaBoardRequestsDto getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }
}
