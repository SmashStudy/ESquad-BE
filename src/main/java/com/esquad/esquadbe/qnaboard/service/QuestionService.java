package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardRequestsDto;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 전체 게시글 조회
    public List<QnaBoardRequestsDto> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::qnaBoardDto)
                .collect(Collectors.toList());
    }

    // 특정 ID의 게시글 조회
    public QnaBoardRequestsDto getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(this::qnaBoardDto)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
    }

    // 엔티티를 DTO로 변환
    private QnaBoardRequestsDto qnaBoardDto(BookQnaBoard bookQnaBoard) {
        return QnaBoardRequestsDto.builder()
                .id(bookQnaBoard.getId())
                .writer(bookQnaBoard.getWriter())
                .title(bookQnaBoard.getTitle())
                .studyPage(bookQnaBoard.getStudyPage())
                .book(bookQnaBoard.getBook())
                .content(bookQnaBoard.getContent())
                .likes(bookQnaBoard.getLikes())
                .build();
    }
}
