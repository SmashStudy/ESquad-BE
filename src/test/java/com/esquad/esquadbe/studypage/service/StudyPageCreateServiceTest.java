package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.BookNotFoundException;
import com.esquad.esquadbe.studypage.exception.StudyNotFoundException;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository;
import com.esquad.esquadbe.studypage.repository.StudyRemindRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.team.repository.TeamRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudyPageCreateServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudyPageRepository studyPageRepository;

    @Mock
    private StudyPageUserRepository studyPageUserRepository;

    @Mock
    private StudyRemindRepository studyRemindRepository;

    @InjectMocks
    private StudyPageService studyPageService;

    private final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    private TeamSpace teamSpace;
    private Book book;
    private StudyPage studyPage;

    @BeforeEach
    public void setUp() {

        teamSpace = TeamSpace.builder()
                .id(100L)
                .teamName("팀 이름")
                .description("팀설명")
                .build();

        book = Book.builder()
                .title("그리스도를 본받아 (개역개정판 성경에 맞춰 새롭게 편집한 최신 완역본)")
                .imgPath("path/to/image.jpg") // imgPath 추가
                .build();

        studyPage = StudyPage.builder()
                .id(1L)
                .book(book)
                .teamSpace(teamSpace)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .description("페이지 설명")
                .studyPageName("스터디 이름")
                .build();

    }

    //createStudyPage
    @Test
    @DisplayName("AllInputsAreValid")
    public void shouldCreateStudyPageWhenAllInputsAreValid() {
        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

        // Given
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(studyPageRepository.save(any(StudyPage.class))).thenReturn(studyPage);

        // When
        Long result = studyPageService.createStudyPage(100L, 1L, dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1L);
        verify(studyPageRepository, times(1)).save(any(StudyPage.class));
    }

    @Test
    @DisplayName("TeamSpaceIsNotFound")
    public void shouldThrowExceptionWhenTeamSpaceIsNotFound() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

        when(teamRepository.findById(100L)).thenThrow( new TeamNotFoundException());
       // Long teamId = null;
        // When & Then
        assertThrows(TeamNotFoundException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("BookIsNotFound")
    public void shouldThrowExceptionWhenBookIsNotFound() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("shouldNotThrowExceptionWhenDtoIsValid")
    public void shouldNotThrowExceptionWhenDtoIsValid() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(studyPageRepository.save(any(StudyPage.class))).thenReturn(studyPage);
        // When & Then
        assertDoesNotThrow(() -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    // readStudyPages
    @Test
    @DisplayName("shouldReturnStudyPagesWhenTeamSpaceExists")
    void shouldReturnStudyPagesWhenTeamSpaceExists() {
        // Given
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        // Mocking the repository call
        when(studyPageRepository.findAllByTeamSpace(teamSpace)).thenReturn(Optional.of(Collections.singletonList(studyPage)));

        // When
        List<StudyPageReadDto> result = assertDoesNotThrow(() -> {
            return studyPageService.readStudyPages(100L);
        });

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(studyPage.getId());
        assertThat(result.get(0).getImage()).isEqualTo(studyPage.getBook().getImgPath());
        assertThat(result.get(0).getTitle()).isEqualTo(studyPage.getStudyPageName());
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenTeamSpaceNotFound")
    void shouldThrowExceptionWhenTeamSpaceNotFound() {

        // Given
        when(teamRepository.findById(100L)).thenThrow( new TeamNotFoundException());

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> {
            studyPageService.readStudyPages(100L);
        });
    }

    @Test
    @DisplayName("shouldReturnEmptyListWhenNoStudyPagesFound")
    void shouldReturnEmptyListWhenNoStudyPagesFound() {
        // Given
        Long teamId = 100L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(teamSpace));
        when(studyPageRepository.findAllByTeamSpace(teamSpace)).thenReturn(Optional.of(Collections.emptyList()));

        // When
        List<StudyPageReadDto> result = studyPageService.readStudyPages(teamId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // readStudyPageInfo
    @Test
    @DisplayName("shouldReturnStudyInfoDtoWhenStudyPageExists")
    void shouldReturnStudyInfoDtoWhenStudyPageExists() {
        // Given
        Long studyPageId = 1L;
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(studyPage)); // StudyPage가 존재하는 경우

        // When
        StudyInfoDto result = studyPageService.readStudyPageInfo(studyPageId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStudyPageName()).isEqualTo(studyPage.getStudyPageName());
        assertThat(result.getStartDate()).isEqualTo(studyPage.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(studyPage.getEndDate());
        assertThat(result.getDescription()).isEqualTo(studyPage.getDescription());
    }

    @Test
    @DisplayName("shouldThrowEntityNotFoundExceptionWhenStudyPageDoesNotExist")
    void shouldThrowEntityNotFoundExceptionWhenStudyPageDoesNotExist() {
        // Given
        Long studyPageId = 1L;
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.empty()); // StudyPage가 존재하지 않는 경우

        // When & Then
        assertThrows(StudyNotFoundException.class, () -> studyPageService.readStudyPageInfo(studyPageId));
    }

}
