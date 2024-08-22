package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service

public class QuestionService {

    private final QuestionRepository questionRepository;

    // 전체 게시글 조회
    public List<BookQnaBoard> getAllQuestions() {
        return questionRepository.findAll();
    }


    // 특정 ID의 게시글 조회
    public BookQnaBoard getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id를 찾을 수 없습니다: " + id));
    }
}
