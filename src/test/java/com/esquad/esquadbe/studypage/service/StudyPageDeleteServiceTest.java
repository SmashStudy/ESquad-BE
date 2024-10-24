package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.BookJsonProcessingException;
import com.esquad.esquadbe.studypage.exception.StudyPageNameNotEqualException;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository; // 추가
import com.esquad.esquadbe.studypage.repository.StudyRemindRepository; // 추가
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudyPageDeleteServiceTest {

    @InjectMocks
    private StudyPageService studyPageService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudyPageRepository studyPageRepository;

    @Mock
    private StudyPageUserRepository studyPageUserRepository; // 추가된 Mock
    @Mock
    private StudyRemindRepository studyRemindRepository; // 추가된 Mock

    private TeamSpace teamSpace;
    private Book book;
    private StudyInfoDto dto;
    private StudyPage studyPage;

    @BeforeEach
    public void setUp() {
        dto = new StudyInfoDto("Study Test", LocalDate.now(), LocalDate.now(), "test");

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

    @Test
    @DisplayName("shouldDeleteStudyPageSuccessfully")
    void shouldDeleteStudyPageSuccessfully() {
        // Given
        Long studyPageId = 1L;
        String studyPageName = "스터디 이름"; // 기존 studyPage의 이름과 일치하도록 변경

        // Mocking repository behavior
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(studyPage));
        doNothing().when(studyPageUserRepository).deleteAllByStudyPage(studyPage);
        doNothing().when(studyRemindRepository).deleteByStudyPage(studyPage);

        // When
        assertDoesNotThrow(() -> {
            studyPageService.deleteStudyPage(studyPageId, studyPageName);
        });

        // Then
        verify(studyPageRepository).delete(studyPage); // StudyPage 삭제가 호출되었는지 확인
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNotFound")
    void shouldThrowExceptionWhenStudyPageNotFound() {
        // Given
        Long studyPageId = 1L;
        String studyPageName = "스터디 이름";

        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookJsonProcessingException.class, () -> {
            studyPageService.deleteStudyPage(studyPageId, studyPageName);
        });
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNameDoesNotMatch")
    void shouldThrowExceptionWhenStudyPageNameDoesNotMatch() {
        // Given
        Long studyPageId = 1L;
        String incorrectName = "다른 이름"; // 기존 이름과 일치하지 않는 이름

        // Mocking repository behavior
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(studyPage));

        // When & Then
        assertThrows(StudyPageNameNotEqualException.class, () -> {
            studyPageService.deleteStudyPage(studyPageId, incorrectName);
        });
    }
}
