package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.BookNotFoundException;
import com.esquad.esquadbe.studypage.exception.StudyNotFoundException;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.team.repository.TeamRepository;
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

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudyPageRepository studyPageRepository;

    @InjectMocks
    private StudyPageService studyPageService;

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
                .imgPath("path/to/image.jpg")
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

    @Test
    @DisplayName("AllInputsAreValid")
    public void shouldCreateStudyPageWhenAllInputsAreValid() {
        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(studyPageRepository.save(any(StudyPage.class))).thenReturn(studyPage);

        Long result = studyPageService.createStudyPage(100L, 1L, dto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1L);
        verify(studyPageRepository, times(1)).save(any(StudyPage.class));
    }

    @Test
    @DisplayName("TeamSpaceIsNotFound")
    public void shouldThrowExceptionWhenTeamSpaceIsNotFound() {

        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

        when(teamRepository.findById(100L)).thenThrow( new TeamNotFoundException());

        assertThrows(TeamNotFoundException.class, () -> studyPageService.createStudyPage(100L, 1L, dto));
    }

    @Test
    @DisplayName("BookIsNotFound")
    public void shouldThrowExceptionWhenBookIsNotFound() {

        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );

        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> studyPageService.createStudyPage(100L, 1L, dto));
    }

    @Test
    @DisplayName("shouldNotThrowExceptionWhenDtoIsValid")
    public void shouldNotThrowExceptionWhenDtoIsValid() {

        StudyInfoDto dto = new StudyInfoDto("Study Test",LocalDate.now(),LocalDate.now(), "test" );
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(studyPageRepository.save(any(StudyPage.class))).thenReturn(studyPage);

        assertDoesNotThrow(() -> {
            studyPageService.createStudyPage(100L, 1L, dto);
        });
    }

    @Test
    @DisplayName("shouldReturnStudyPagesWhenTeamSpaceExists")
    void shouldReturnStudyPagesWhenTeamSpaceExists() {

        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(studyPageRepository.findAllByTeamSpace(teamSpace)).thenReturn(Optional.of(Collections.singletonList(studyPage)));

        List<StudyPageReadDto> result = assertDoesNotThrow(() -> studyPageService.readStudyPages(100L));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(studyPage.getId());
        assertThat(result.get(0).getImage()).isEqualTo(studyPage.getBook().getImgPath());
        assertThat(result.get(0).getTitle()).isEqualTo(studyPage.getStudyPageName());
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenTeamSpaceNotFound")
    void shouldThrowExceptionWhenTeamSpaceNotFound() {

        when(teamRepository.findById(100L)).thenThrow( new TeamNotFoundException());

        assertThrows(TeamNotFoundException.class, () -> studyPageService.readStudyPages(100L));
    }

    @Test
    @DisplayName("shouldReturnEmptyListWhenNoStudyPagesFound")
    void shouldReturnEmptyListWhenNoStudyPagesFound() {

        Long teamId = 100L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(teamSpace));
        when(studyPageRepository.findAllByTeamSpace(teamSpace)).thenReturn(Optional.of(Collections.emptyList()));

        List<StudyPageReadDto> result = studyPageService.readStudyPages(teamId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("shouldReturnStudyInfoDtoWhenStudyPageExists")
    void shouldReturnStudyInfoDtoWhenStudyPageExists() {

        Long studyPageId = 1L;
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(studyPage));

        StudyInfoDto result = studyPageService.readStudyPageInfo(studyPageId);

        assertThat(result).isNotNull();
        assertThat(result.getStudyPageName()).isEqualTo(studyPage.getStudyPageName());
        assertThat(result.getStartDate()).isEqualTo(studyPage.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(studyPage.getEndDate());
        assertThat(result.getDescription()).isEqualTo(studyPage.getDescription());
    }

    @Test
    @DisplayName("shouldThrowEntityNotFoundExceptionWhenStudyPageDoesNotExist")
    void shouldThrowEntityNotFoundExceptionWhenStudyPageDoesNotExist() {

        Long studyPageId = 1L;
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.empty());

        assertThrows(StudyNotFoundException.class, () -> studyPageService.readStudyPageInfo(studyPageId));
    }

}
