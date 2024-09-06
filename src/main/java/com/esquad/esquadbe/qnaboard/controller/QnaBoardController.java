package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/questions")
@Slf4j
public class QnaBoardController {

    private final QuestionService questionService;

    // 페이징된 전체 게시글 조회
    @GetMapping
    public Page<QnaBoardResponseDTO> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info(page + "/" + size);
        return questionService.getAllQuestions(page, size);
    }

    // 특정 ID의 게시글 조회
    @GetMapping("/{id}")
    public QnaBoardResponseDTO getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    // 새로운 질문 생성
    @PostMapping
    public QnaBoardResponseDTO createQuestion(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("writer") String writer,
            @RequestParam("book") String book) {
        return questionService.createQuestion(title, content, writer, book);
    }

    // 특정 제목의 게시글 조회
    @GetMapping("/search")
    public Page<QnaBoardResponseDTO> getQuestionsByTitle(
            @RequestParam("title") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching for questions with title: " + title);
        return questionService.getQuestionsByTitle(title, page, size);
    }

    // 특정 작성자의 게시글 조회
    @GetMapping("/by-writer")
    public Page<QnaBoardResponseDTO> getQuestionsByWriter(
            @RequestParam("writer") String writer,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching for questions by writer: " + writer);
        return questionService.getQuestionsByWriter(writer, page, size);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public QnaBoardResponseDTO updateQuestion(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content")String content,
            @RequestParam("book") String book){
        return questionService.updateQuestion(id,title, content, book);
    }

    // 게시글 삭제

}
