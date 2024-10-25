package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.BookJsonProcessingException;
import com.esquad.esquadbe.studypage.exception.StudyPageNameNotEqualException;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository;
import com.esquad.esquadbe.studypage.repository.StudyRemindRepository;
import com.esquad.esquadbe.team.entity.TeamSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

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
    private StudyPageRepository studyPageRepository;

    @Mock
    private StudyPageUserRepository studyPageUserRepository;
    @Mock
    private StudyRemindRepository studyRemindRepository;

    private StudyPage studyPage;

    @BeforeEach
    public void setUp() {
        TeamSpace teamSpace = TeamSpace.builder()
                .id(100L)
                .teamName("팀 이름")
                .description("팀설명")
                .build();

        Book book = Book.builder()
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
    @DisplayName("shouldDeleteStudyPageSuccessfully")
    void shouldDeleteStudyPageSuccessfully() {

        Long studyPageId = 1L;
        String studyPageName = "스터디 이름";

        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(studyPage));
        doNothing().when(studyPageUserRepository).deleteAllByStudyPage(studyPage);
        doNothing().when(studyRemindRepository).deleteByStudyPage(studyPage);

        assertDoesNotThrow(() -> studyPageService.deleteStudyPage(studyPageId, studyPageName));

        verify(studyPageRepository).delete(studyPage);
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNotFound")
    void shouldThrowExceptionWhenStudyPageNotFound() {

        Long studyPageId = 1L;
        String studyPageName = "스터디 이름";

        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.empty());

        assertThrows(BookJsonProcessingException.class, () -> studyPageService.deleteStudyPage(studyPageId, studyPageName));
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNameDoesNotMatch")
    void shouldThrowExceptionWhenStudyPageNameDoesNotMatch() {

        Long studyPageId = 1L;
        String incorrectName = "다른 이름";

        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(studyPage));

        assertThrows(StudyPageNameNotEqualException.class, () -> studyPageService.deleteStudyPage(studyPageId, incorrectName));
    }
}
