package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.exception.UnauthorizedException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.dto.QnaRequestDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.entity.BookQnaLike;
import com.esquad.esquadbe.qnaboard.repository.BookQnaLikeRepository;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.storage.entity.TargetType;
import com.esquad.esquadbe.storage.service.S3FileService;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final BookRepository bookRepository;
    private final TeamSpaceRepository teamSpaceRepository;
    private final BookQnaLikeRepository likeRepository;
    private final S3FileService s3FileService;

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
    }

    private TeamSpace findTeamSpaceById(Long teamSpaceId) {
        return teamSpaceRepository.findById(teamSpaceId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamSpace not found with id: " + teamSpaceId));
    }

    // 전체 게시글 조회
    public Page<QnaBoardResponseDTO> getAllQuestions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookQnaBoard> questionPage = questionRepository.findAll(pageable);
        return questionPage.map(QnaBoardResponseDTO::from);
    }

    // 특정 ID의 게시글 조회
    public QnaBoardResponseDTO getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(QnaBoardResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
    }

    // 게시글 생성
    @Transactional
    public QnaBoardResponseDTO createQuestion(QnaRequestDTO qnaForm, MultipartFile file) {

        User user = findUserById(qnaForm.userId());
        Book book = findBookById(qnaForm.bookId());
        TeamSpace teamSpace = findTeamSpaceById(qnaForm.teamSpaceId());

        BookQnaBoard newBoard = qnaForm.to(user, book, teamSpace);

        // 생성된 게시글 저장
        BookQnaBoard savedQuestion = questionRepository.save(newBoard);

        // 파일이 있을 경우에만 파일 업로드
        if (file != null && !file.isEmpty()) {
            s3FileService.uploadFile(file, savedQuestion.getId(), TargetType.QNA, String.valueOf(qnaForm.userId()));  // userId를 String으로 변환
        }


        return QnaBoardResponseDTO.from(savedQuestion);
    }

    // 특정 제목의 게시글 조회
    public Page<QnaBoardResponseDTO> getQuestionsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByTitleContaining(title, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    // 특정 작성자의 게시글 조회
    public Page<QnaBoardResponseDTO> getQuestionsByWriter(Long userId, int page, int size) {
        User user = findUserById(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return questionRepository.findByWriter(user, pageable)
                .map(QnaBoardResponseDTO::from);
    }

    @Transactional
    public QnaBoardResponseDTO updateQuestion(Long id, QnaRequestDTO qnaForm) {
        // 게시글 ID로 기존 게시글 조회
        BookQnaBoard existBoard = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));

        // 작성자 확인
        if (!existBoard.getWriter().getId().equals(qnaForm.userId())) {
            throw new UnauthorizedException("게시글 수정 권한이 없습니다.");
        }

        // 책 정보 업데이트
        Book updatedBook = qnaForm.bookId() != null ? findBookById(qnaForm.bookId()) : existBoard.getBook();

        // 수정된 게시글 생성
        BookQnaBoard updatedBoard = BookQnaBoard.builder()
                .id(existBoard.getId())
                .title(qnaForm.title())
                .content(qnaForm.content())
                .writer(existBoard.getWriter())
                .book(updatedBook)
                .teamSpace(existBoard.getTeamSpace())
                .likes(existBoard.getLikes())
                .build();

        // 저장 후 반환
        return QnaBoardResponseDTO.from(questionRepository.save(updatedBoard));
    }

    // 게시글 삭제
    @Transactional
    public void deleteQuestion(Long id) {
        BookQnaBoard question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));
        questionRepository.delete(question);
    }

    // 좋아요 처리 로직
    @Transactional
    public String boardLike(Long boardId, Long userId) {
        log.info("사용자가 게시글에 좋아요 요청");

        BookQnaBoard board = questionRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다: " + boardId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        BookQnaLike existingLike = likeRepository.findByUserAndBoard(user, board);

        if (existingLike != null) {
            // 좋아요 취소
            likeRepository.delete(existingLike);

            // 좋아요 수 감소한 새로운 객체 생성
            BookQnaBoard updatedBoard = BookQnaBoard.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getWriter())
                    .book(board.getBook())
                    .teamSpace(board.getTeamSpace())
                    .likes(board.getLikes() - 1)  // 좋아요 수 감소
                    .build();

            questionRepository.save(updatedBoard);
            return "좋아요 취소";
        } else {
            // 좋아요 추가
            BookQnaLike newLike = new BookQnaLike(null, user, board);
            likeRepository.save(newLike);

            // 좋아요 수 증가한 새로운 객체 생성
            BookQnaBoard updatedBoard = BookQnaBoard.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(board.getWriter())
                    .book(board.getBook())
                    .teamSpace(board.getTeamSpace())
                    .likes(board.getLikes() + 1)  // 좋아요 수 증가
                    .build();

            questionRepository.save(updatedBoard);
            return "좋아요 추가";
        }
    }
}
