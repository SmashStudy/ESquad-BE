package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.StudyPageException;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private StudyPageService studyPageService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudyPageRepository studyPageRepository;

    private TeamSpace teamSpace;
    private Book book;
    private StudyInfoDto dto;
    private StudyPage studyPage;

    @BeforeEach
    public void setUp() {
        dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

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
        // Given
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(studyPageRepository.save(any(StudyPage.class))).thenAnswer(invocation -> {
            StudyPage savedStudyPage = invocation.getArgument(0);
            return StudyPage.builder() // 빌더를 사용하여 객체를 생성
                    .id(1L) // ID를 설정
                    .studyPageName(savedStudyPage.getStudyPageName())
                    // 기타 필요한 필드들
                    .build();
        });


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
        when(teamRepository.findById(100L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("BookIsNotFound")
    public void shouldThrowExceptionWhenBookIsNotFound() {
        // Given
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    // validateStudyInfoDto
    @Test
    @DisplayName("shouldThrowExceptionWhenDtoIsNull")
    public void shouldThrowExceptionWhenDtoIsNull() {
        // When & Then
        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, null);
        });
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNameIsEmpty")
    public void shouldThrowExceptionWhenStudyPageNameIsEmpty() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("", LocalDate.now(), LocalDate.now(), "description");

        // When & Then
        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStartDateIsNull")
    public void shouldThrowExceptionWhenStartDateIsNull() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("Study Test", null, LocalDate.now(), "description");

        // When & Then
        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenEndDateIsNull")
    public void shouldThrowExceptionWhenEndDateIsNull() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("Study Test", LocalDate.now(), null, "description");

        // When & Then
        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenEndDateIsBeforeStartDate")
    public void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Given
        StudyInfoDto dto = new StudyInfoDto("Study Test", LocalDate.now().plusDays(1), LocalDate.now(), "description");

        // When & Then
        assertThrows(StudyPageException.class, () -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("shouldNotThrowExceptionWhenDtoIsValid")
    public void shouldNotThrowExceptionWhenDtoIsValid() {
        // Given
//        StudyInfoDto dto = new StudyInfoDto("Study Test", LocalDate.now(), LocalDate.now().plusDays(1), "description");
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

        when(studyPageRepository.save(any(StudyPage.class))).thenAnswer(invocation -> {
            StudyPage savedStudyPage = invocation.getArgument(0);
            return StudyPage.builder() // 빌더를 사용하여 객체를 생성
                    .id(1L) // ID를 설정
                    .studyPageName(savedStudyPage.getStudyPageName())
                    // 기타 필요한 필드들
                    .build();
        });

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
        Long teamId = 100L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            studyPageService.readStudyPages(teamId);
        });
        assertThat(exception.getMessage()).isEqualTo("TeamSpace not found with ID: " + teamId); // assertEquals() 대신
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
        assertThrows(EntityNotFoundException.class, () -> studyPageService.readStudyPageInfo(studyPageId));
    }

}
