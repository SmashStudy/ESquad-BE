package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardRequestsDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 페이징 처리된 전체 게시글 조회
    public Page<QnaBoardRequestsDTO> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findAll(pageable)
                .map(QnaBoardRequestsDTO::fromEntity);
    }

    // 특정 ID의 게시글 조회
    public QnaBoardRequestsDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(QnaBoardRequestsDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
    }



}
