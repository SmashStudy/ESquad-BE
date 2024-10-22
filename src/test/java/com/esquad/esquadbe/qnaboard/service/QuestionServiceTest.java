package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.dto.QnaRequestDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.exception.ResourceNotFoundException;
import com.esquad.esquadbe.qnaboard.exception.UnauthorizedException;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TeamSpaceRepository teamSpaceRepository;

    private BookQnaBoard existingBoard;
    private User user;
    private Book book;
    private TeamSpace teamSpace;

    @BeforeEach
    void setUp() {
        // 유저 생성
        user = userRepository.save(User.builder()
                .username("testUser1")
                .nickname("Test Nickname")
                .password("password")
                .email("test@example.com")
                .phoneNumber("01012345678")
                .birthDay(LocalDate.of(1990, 1, 1))
                .address("Test Address")
                .build());

        // 책(Book) 생성
        book = bookRepository.save(Book.builder()
                .title("Test Book")
                .imgPath("test/path/to/image.jpg")
                .author("Test Author")
                .publisher("Test Publisher")
                .pubDate("20200101")
                .isbn("1234567890123")
                .description("Test Description")
                .build());

        // 팀 스페이스(TeamSpace) 생성
        teamSpace = teamSpaceRepository.save(TeamSpace.builder()
                .teamName("Test TeamSpace")
                .description("This is a test team space.")
                .build());

        // 기존 게시글 생성
        existingBoard = questionRepository.save(BookQnaBoard.builder()
                .title("Old Title")
                .content("Old Content")
                .writer(user)
                .book(book)
                .teamSpace(teamSpace)
                .likes(0)
                .build());
    }

    @Test
    void createQuestionTest() {
        // Given
        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Test Question")
                .content("This is a test question content.")
                .username(user.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        // When
        QnaBoardResponseDTO createdQuestion = questionService.createQuestion(qnaRequestDTO, null);

        // Then
        assertNotNull(createdQuestion);
        assertEquals("Test Question", createdQuestion.getTitle());
        assertEquals("This is a test question content.", createdQuestion.getContent());
        assertEquals(user.getNickname(), createdQuestion.getWriterName());
    }

    @Test
    void getQuestionByIdTest() {
        // Given
        BookQnaBoard board = questionRepository.save(
                BookQnaBoard.builder()
                        .title("Test Title")
                        .content("Test Content")
                        .writer(user)
                        .book(book)
                        .teamSpace(teamSpace)
                        .likes(0)
                        .build());

        // When
        QnaBoardResponseDTO result = questionService.getQuestionById(board.getId());

        // Then
        assertNotNull(result);
        assertEquals(board.getTitle(), result.getTitle());
        assertEquals(board.getContent(), result.getContent());
        assertEquals(board.getWriter().getNickname(), result.getWriterName());
    }

    @Test
    void getQuestionsByTitleTest() {
        // Given
        questionRepository.save(
                BookQnaBoard.builder()
                        .title("Searchable Title")
                        .content("Test Content")
                        .writer(user)
                        .book(book)
                        .teamSpace(teamSpace)
                        .likes(0)
                        .build());

        // When
        var resultPage = questionService.getQuestionsByTitle("Searchable", 0, 10);

        // Then
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals("Searchable Title", resultPage.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("게시글의 작성자가 게시글을 업데이트")
    void updateQuestion_Success() {
        // Given
        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .username(user.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        // When
        QnaBoardResponseDTO updatedBoard = questionService.updateQuestion(existingBoard.getId(), qnaRequestDTO);

        // Then
        assertNotNull(updatedBoard);
        assertEquals("Updated Title", updatedBoard.getTitle());
        assertEquals("Updated Content", updatedBoard.getContent());
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 게시글을 업데이트하려고 할 때 예외 발생")
    void updateQuestion_Unauthorized() {
        // Given: 다른 유저가 게시글을 수정하려고 시도
        User otherUser = userRepository.save(User.builder()
                .username("otherUser")
                .nickname("Other Nickname")
                .password("password")
                .email("other@example.com")
                .phoneNumber("01098765432")
                .birthDay(LocalDate.of(1995, 5, 5))
                .address("Other Address")
                .build());

        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .username(otherUser.getUsername()) // 다른 사용자 ID
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        // When & Then: UnauthorizedException이 발생해야 함
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            questionService.updateQuestion(existingBoard.getId(), qnaRequestDTO);
        });

        assertEquals("게시글 수정 권한이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 업데이트하려고 할 때 예외 발생")
    void updateQuestion_NotFound() {
        // Given: 존재하지 않는 게시글 ID
        Long nonExistingId = 999L;

        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .username(user.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        // When & Then: ResourceNotFoundException이 발생해야 함
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionService.updateQuestion(nonExistingId, qnaRequestDTO);
        });

        assertEquals("해당 게시물을 찾을 수 없습니다: " + nonExistingId, exception.getMessage());
    }

    @Test
    void deleteQuestionTest() {
        // Given
        BookQnaBoard board = questionRepository.save(
                BookQnaBoard.builder()
                        .title("Title to Delete")
                        .content("Content to Delete")
                        .writer(user)
                        .book(book)
                        .teamSpace(teamSpace)
                        .likes(0)
                        .build());

        // When
        questionService.deleteQuestion(board.getId(), user.getUsername());

        // Then
        assertFalse(questionRepository.findById(board.getId()).isPresent());
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 게시글을 삭제하려고 할 때 예외 발생")
    void deleteQuestion_Unauthorized() {
        // Given: 다른 유저가 게시글을 삭제하려고 시도
        User otherUser = userRepository.save(User.builder()
                .username("otherUser")
                .nickname("Other Nickname")
                .password("password")
                .email("other@example.com")
                .phoneNumber("01098765432")
                .birthDay(LocalDate.of(1995, 5, 5))
                .address("Other Address")
                .build());

        // When & Then: UnauthorizedException이 발생해야 함
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            questionService.deleteQuestion(existingBoard.getId(), otherUser.getUsername());
        });

        assertEquals("게시글 삭제 권한이 없습니다.", exception.getMessage());
    }
}
