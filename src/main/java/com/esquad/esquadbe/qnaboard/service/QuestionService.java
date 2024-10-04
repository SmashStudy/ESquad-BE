package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
import com.esquad.esquadbe.exception.UnauthorizedException;
import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
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
    public QnaBoardResponseDTO createQuestion(String title, String content, Long userId, Long bookId, Long teamSpaceId, MultipartFile file) {

        User user = findUserById(userId);
        Book book = findBookById(bookId);
        TeamSpace teamSpace = findTeamSpaceById(teamSpaceId);

        TargetType targetType = TargetType.QNA;


        BookQnaBoard question = BookQnaBoard.builder()
                .title(title)
                .content(content)
                .writer(user)
                .book(book)
                .teamSpace(teamSpace)
                .likes(0)
                .build();

        // 생성된 게시글 저장하고
        BookQnaBoard savedQuestion = questionRepository.save(question);
        // file null일 때 이거 안 돌아가게ㅇㅇ
        // 파일이 있을 경우에만 파일을 업로드
        if (file != null && !file.isEmpty()) {
            s3FileService.uploadFile(file, savedQuestion.getId(), targetType, userId);
        }

        // 저장된 게시글 정보 반환
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

    public QnaBoardResponseDTO updateQuestion(Long id, Long userId, String title, String content, Long bookId) {
        BookQnaBoard existBoard = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다: " + id));

        if (!existBoard.getWriter().getId().equals(userId)) {
            throw new UnauthorizedException("게시글 수정 권한이 없습니다.");
        }

        if (bookId == null) {
            return updateQuestionNoBook(id, userId, title, content);
        }

        return updateQuestionWithBook(id, userId, title, content, bookId);
    }

    // 게시글 수정 로직
    @Transactional
    protected QnaBoardResponseDTO updateQuestionWithBook(Long id, Long userId, String title, String content, Long bookId) {
        BookQnaBoard existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다: " + id));

        User user = findUserById(userId);
        Book book = findBookById(bookId);

        existingQuestion.setTitle(title);
        existingQuestion.setContent(content);
        existingQuestion.setWriter(user);
        existingQuestion.setBook(book);

        BookQnaBoard updatedQuestion = questionRepository.save(existingQuestion);
        return QnaBoardResponseDTO.from(updatedQuestion);
    }

    @Transactional
    protected QnaBoardResponseDTO updateQuestionNoBook(Long id, Long userId, String title, String content) {
        BookQnaBoard existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다: " + id));

        User user = findUserById(userId);

        existingQuestion.setTitle(title);
        existingQuestion.setContent(content);
        existingQuestion.setWriter(user);

        BookQnaBoard updatedQuestion = questionRepository.save(existingQuestion);
        return QnaBoardResponseDTO.from(updatedQuestion);
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

        // 이미 좋아요를 눌렀는지 확인
        BookQnaLike existingLike = likeRepository.findByUserAndBoard(user, board);

        if (existingLike != null) {
            // 이미 좋아요를 눌렀다면 취소
            likeRepository.delete(existingLike);
            board.setLikes(board.getLikes() - 1); // 좋아요 수 감소
            questionRepository.save(board); // 좋아요 수 업데이트
            return "좋아요 취소";
        } else {
            // 좋아요가 없으면 추가
            BookQnaLike newLike = new BookQnaLike(null, user, board);
            likeRepository.save(newLike);
            board.setLikes(board.getLikes() + 1); // 좋아요 수 증가
            questionRepository.save(board); // 좋아요 수 업데이트
            return "좋아요 추가";
        }

    }
}
