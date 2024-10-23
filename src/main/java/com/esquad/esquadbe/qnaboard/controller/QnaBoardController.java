package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.dto.QnaRequestDTO;
import com.esquad.esquadbe.qnaboard.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/questions")
@Slf4j
public class QnaBoardController {

    private final QuestionService questionService;

    // 전체 게시글 조회
    @GetMapping
    public Page<QnaBoardResponseDTO> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return questionService.getAllQuestions(page, size);
    }

    // 특정 ID의 게시글 조회 (게시글번호)
    @GetMapping("/{id}")
    public QnaBoardResponseDTO getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    // 새로운 질문 생성
    @PostMapping
    public QnaBoardResponseDTO createQuestion(
            @ModelAttribute QnaRequestDTO qnaForm,
            @RequestParam(required = false) MultipartFile file) {
        return questionService.createQuestion(qnaForm, file);
    }


    // 특정 제목의 게시글 조회
    @GetMapping("/search")
    public Page<QnaBoardResponseDTO> getQuestionsByTitle(
            @RequestParam("title") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return questionService.getQuestionsByTitle(title, page, size);
    }

    // 특정 작성자의 게시글 조회
    @GetMapping("/by-writer")
    public Page<QnaBoardResponseDTO> getQuestionsByWriter(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return questionService.getQuestionsByWriter(principal.getName(), page, size);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public QnaBoardResponseDTO updateQuestion(@PathVariable Long id, @RequestBody QnaRequestDTO updateForm) {
        return questionService.updateQuestion(id, updateForm);
    }


    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id, Principal principal) {
        // 현재 로그인한 사용자의 ID를 서비스로 전달
        questionService.deleteQuestion(id, principal.getName());
        return ResponseEntity.ok("게시글이 삭제되었습니다");
    }
}
