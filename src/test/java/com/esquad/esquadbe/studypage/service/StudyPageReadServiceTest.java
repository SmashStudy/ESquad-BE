package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
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
import java.util.Arrays;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudyPageReadServiceTest {

    @InjectMocks
    private StudyPageService studyPageService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private StudyPageRepository studyPageRepository;

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

    // readStudyPages
    @Test
    @DisplayName("shouldReturnStudyPagesWhenTeamSpaceExists")
    void shouldReturnStudyPagesWhenTeamSpaceExists() {
        // Given
        when(teamRepository.findById(100L)).thenReturn(Optional.of(teamSpace));
        when(studyPageRepository.findAllByTeamSpace(teamSpace)).thenReturn(Optional.of(Collections.singletonList(studyPage))); // Adjusted return value

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

         assertThrows(TeamNotFoundException.class, () -> {
            studyPageService.readStudyPages(teamId);
        });
    }

    @Test
    @DisplayName("shouldReturnStudyPagesWhenFound")
    public void shouldReturnStudyPagesWhenFound() {
        // Given
        when(teamRepository.findById(any(Long.class))).thenReturn(Optional.of(teamSpace));

        List<StudyPage> studyPages = Collections.singletonList(studyPage);
        when(studyPageRepository.findAllByTeamSpace(teamSpace)).thenReturn(Optional.of(studyPages));

        // When
        List<StudyPageReadDto> result = studyPageService.readStudyPages(100L);

        // Then
        assertEquals(1, result.size(), "Expected two StudyPages to be returned");
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

///아마존 : 글로벌, 자동화의 코드 자동 패치
