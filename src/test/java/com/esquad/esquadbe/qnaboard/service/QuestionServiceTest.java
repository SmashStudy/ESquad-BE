package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.dto.QnaBoardResponseDTO;
import com.esquad.esquadbe.qnaboard.dto.QnaRequestDTO;
import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.qnaboard.exception.QuestionNotFoundException;
import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.exception.UserErrorCode;
import com.esquad.esquadbe.user.exception.UserUsernameException;
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
    private TeamRepository teamRepository;

    private BookQnaBoard existingBoard;
    private User user;
    private Book book;
    private TeamSpace teamSpace;

    @BeforeEach
    void setUp() {

        user = userRepository.save(User.builder()
                .username("testUser1")
                .nickname("Test Nickname")
                .password("password")
                .email("test@example.com")
                .phoneNumber("01012345678")
                .birthDay(LocalDate.of(1990, 1, 1))
                .address("Test Address")
                .build());


        book = bookRepository.save(Book.builder()
                .title("Test Book")
                .imgPath("test/path/to/image.jpg")
                .author("Test Author")
                .publisher("Test Publisher")
                .pubDate("20200101")
                .isbn("1234567890123")
                .description("Test Description")
                .build());


        teamSpace = teamRepository.save(TeamSpace.builder()
                .teamName("Test TeamSpace")
                .description("This is a test team space.")
                .build());


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
    @DisplayName("게시글 생성")
    void createQuestionTest() {

        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Test Question")
                .content("This is a test question content.")
                .username(user.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        QnaBoardResponseDTO createdQuestion = questionService.createQuestion(qnaRequestDTO, null);

        assertNotNull(createdQuestion);
        assertEquals("Test Question", createdQuestion.getTitle());
        assertEquals("This is a test question content.", createdQuestion.getContent());
        assertEquals(user.getNickname(), createdQuestion.getWriterName());
    }

    @Test
    @DisplayName("게시글번호로 게시글 조회")
    void getQuestionByIdTest() {

        BookQnaBoard board = questionRepository.save(
                BookQnaBoard.builder()
                        .title("Test Title")
                        .content("Test Content")
                        .writer(user)
                        .book(book)
                        .teamSpace(teamSpace)
                        .likes(0)
                        .build());

        QnaBoardResponseDTO result = questionService.getQuestionById(board.getId());


        assertNotNull(result);
        assertEquals(board.getTitle(), result.getTitle());
        assertEquals(board.getContent(), result.getContent());
        assertEquals(board.getWriter().getNickname(), result.getWriterName());
    }

    @Test
    @DisplayName("게시글 제목으로 조회")
    void getQuestionsByTitleTest() {

        questionRepository.save(
                BookQnaBoard.builder()
                        .title("Searchable Title")
                        .content("Test Content")
                        .writer(user)
                        .book(book)
                        .teamSpace(teamSpace)
                        .likes(0)
                        .build());

        var resultPage = questionService.getQuestionsByTitle("Searchable", 0, 10);

        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals("Searchable Title", resultPage.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("게시글의 작성자가 게시글을 업데이트")
    void updateQuestion_Success() {

        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .username(user.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        QnaBoardResponseDTO updatedBoard = questionService.updateQuestion(existingBoard.getId(), qnaRequestDTO);

        assertNotNull(updatedBoard);
        assertEquals("Updated Title", updatedBoard.getTitle());
        assertEquals("Updated Content", updatedBoard.getContent());
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 게시글을 업데이트하려고 할 때 예외 발생")
    void updateQuestion_Unauthorized() {

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
                .username(otherUser.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        UserUsernameException exception = assertThrows(UserUsernameException.class, () -> questionService.updateQuestion(existingBoard.getId(), qnaRequestDTO));

        assertEquals(UserErrorCode.USER_NOT_FOUND_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 업데이트하려고 할 때 예외 발생")
    void updateQuestion_NotFound() {

        Long nonExistingId = 999L;

        QnaRequestDTO qnaRequestDTO = QnaRequestDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .username(user.getUsername())
                .bookId(book.getId())
                .teamSpaceId(teamSpace.getId())
                .build();

        QuestionNotFoundException exception = assertThrows(QuestionNotFoundException.class, () -> questionService.updateQuestion(nonExistingId, qnaRequestDTO));

        assertEquals("Question doesn't exist", exception.getMessage());
    }

    @Test
    void deleteQuestionTest() {

        BookQnaBoard board = questionRepository.save(
                BookQnaBoard.builder()
                        .title("Title to Delete")
                        .content("Content to Delete")
                        .writer(user)
                        .book(book)
                        .teamSpace(teamSpace)
                        .likes(0)
                        .build());

        questionService.deleteQuestion(board.getId(), String.valueOf(user.getId()));

        assertFalse(questionRepository.findById(board.getId()).isPresent());
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 게시글을 삭제하려고 할 때 예외 발생")
    void deleteQuestion_Unauthorized() {

        User otherUser = userRepository.save(User.builder()
                .username("otherUser")
                .nickname("Other Nickname")
                .password("password")
                .email("other@example.com")
                .phoneNumber("01098765432")
                .birthDay(LocalDate.of(1995, 5, 5))
                .address("Other Address")
                .build());

        UserUsernameException exception = assertThrows(UserUsernameException.class, () -> questionService.deleteQuestion(existingBoard.getId(), String.valueOf(otherUser.getId())));

        assertEquals(UserErrorCode.USER_NOT_FOUND_ERROR.getMessage(), exception.getMessage());
    }
}
