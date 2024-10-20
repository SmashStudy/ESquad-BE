package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.exception.ResourceNotFoundException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookQnaLikeRepository likeRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TeamSpaceRepository teamSpaceRepository;

    @Mock
    private S3FileService s3FileService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private QuestionService questionService;

    private User user;
    private Book book;
    private BookQnaBoard bookQnaBoard;
    private TeamSpace teamSpace;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build();

        book = Book.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .build();

        teamSpace = TeamSpace.builder()
                .id(1L)
                .teamName("Test Team")
                .build();

        bookQnaBoard = BookQnaBoard.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .writer(user)
                .book(book)
                .teamSpace(teamSpace)
                .likes(0)
                .build();

        bookQnaBoard.setCreatedAt(LocalDateTime.now());
        bookQnaBoard.setModifiedAt(LocalDateTime.now());
    }

    @Test
    public void testGetQuestionById() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(bookQnaBoard));

        QnaBoardResponseDTO result = questionService.getQuestionById(1L);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertNotNull(result.getCreatedAt());
        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateQuestion() {
        QnaRequestDTO qnaRequestDTO = new QnaRequestDTO(1L, "Test Title", "Test Content", 1L, 1L, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(teamSpaceRepository.findById(1L)).thenReturn(Optional.of(teamSpace));
        when(questionRepository.save(Mockito.any(BookQnaBoard.class))).thenReturn(bookQnaBoard);

        doReturn(null).when(s3FileService).uploadFile(Mockito.any(MultipartFile.class), Mockito.anyLong(), Mockito.any(TargetType.class), Mockito.anyLong());

        QnaBoardResponseDTO result = questionService.createQuestion(qnaRequestDTO, file);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(questionRepository, times(1)).save(Mockito.any(BookQnaBoard.class));
    }

    @Test
    public void testUpdateQuestion() {
        // 기존 BookQnaBoard의 Mock 설정
        when(questionRepository.findById(1L)).thenReturn(Optional.of(bookQnaBoard));  // bookQnaBoard가 null이 아니도록 설정

        // User와 Book Repository에서도 Mock 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // 수정할 요청 DTO
        QnaRequestDTO qnaRequestDTO = new QnaRequestDTO(1L, "Updated Title", "Updated Content", 1L, 1L, 1L);

        // 기존 bookQnaBoard가 null이 아니도록 ensure
        assertNotNull(bookQnaBoard);

        // 업데이트 메서드 실행 (id를 추가로 전달)
        QnaBoardResponseDTO result = questionService.updateQuestion(1L, qnaRequestDTO);

        // 업데이트가 성공적으로 되었는지 확인
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());

        // bookQnaBoard가 저장되었는지 확인
        verify(questionRepository, times(1)).save(Mockito.any(BookQnaBoard.class));
    }

    @Test
    public void testBoardLike_AddLike() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(bookQnaBoard));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.findByUserAndBoard(user, bookQnaBoard)).thenReturn(null);

        String result = questionService.boardLike(1L, 1L);

        assertEquals("좋아요 추가", result);
        verify(likeRepository, times(1)).save(Mockito.any(BookQnaLike.class));
        verify(questionRepository, times(1)).save(Mockito.any(BookQnaBoard.class));
    }

    @Test
    public void testBoardLike_RemoveLike() {
        BookQnaLike like = new BookQnaLike(1L, user, bookQnaBoard);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(bookQnaBoard));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.findByUserAndBoard(user, bookQnaBoard)).thenReturn(like);

        String result = questionService.boardLike(1L, 1L);

        assertEquals("좋아요 취소", result);
        verify(likeRepository, times(1)).delete(like);
        verify(questionRepository, times(1)).save(Mockito.any(BookQnaBoard.class));
    }

    @Test
    public void testDeleteQuestion() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(bookQnaBoard));

        questionService.deleteQuestion(1L);

        verify(questionRepository, times(1)).delete(bookQnaBoard);
    }

    @Test
    public void testGetQuestionById_NotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionById(1L));
        verify(questionRepository, times(1)).findById(1L);
    }
}
