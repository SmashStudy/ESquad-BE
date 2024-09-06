package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 전체 게시글 조회
    public Page<QnaBoardResponseDTO> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findAll(pageable)
                .map(QnaBoardResponseDTO::from);
    }

    // 특정 ID의 게시글 조회
    public QnaBoardResponseDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(QnaBoardResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
    }

    // 새로운 질문 생성
    public QnaBoardResponseDTO createQuestion(String title, String content, String writer, String book) {
        BookQnaBoard question = BookQnaBoard.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .book(book)
                .likes(0)  // 초기 좋아요 수는 0
                .build();

        BookQnaBoard savedQuestion = questionRepository.save(question);
        return QnaBoardResponseDTO.from(savedQuestion);
    }

    // 특정 제목의 게시글 조회
    public Page<QnaBoardResponseDTO> getQuestionsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByTitleContaining(title, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    // 특정 작성자의 게시글 조회
    public Page<QnaBoardResponseDTO> getQuestionsByWriter(String writer, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByWriter(writer, pageable)
                .map(QnaBoardResponseDTO::from);
    }
}