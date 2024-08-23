package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardRequestsDto;
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
    public Page<QnaBoardRequestsDto> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findAll(pageable)
                .map(this::qnaBoardDto);
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
