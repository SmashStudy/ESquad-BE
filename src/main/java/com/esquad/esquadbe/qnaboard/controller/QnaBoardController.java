package com.esquad.esquadbe.qnaboard.controller;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("userId") Long userId,
            @RequestParam("bookId") Long bookId,
            @RequestParam("teamSpaceId") Long teamSpaceId,
            @RequestParam(required = false) MultipartFile file) {
        return questionService.createQuestion(title, content, userId, bookId, teamSpaceId, file);
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
            @RequestParam("userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching for questions by writer: " + userId);
        return questionService.getQuestionsByWriter(userId, page, size);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public QnaBoardResponseDTO updateQuestion(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,       // User ID로 받아서 User 객체로 조회
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "bookId", required = false) Long bookId) {     // Book ID로 책 조회

        return questionService.updateQuestion(id, userId, title, content, bookId);

    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }

    // 좋아요 추가/취소
    @PostMapping("/like/{boardId}")
    public ResponseEntity<String> boardLike(@PathVariable Long boardId, @RequestParam Long userId) {
        String result = questionService.boardLike(boardId, userId);
        return ResponseEntity.ok(result);
    }


}

