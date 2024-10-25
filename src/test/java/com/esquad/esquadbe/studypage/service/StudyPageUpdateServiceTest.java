package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.StudyNotFoundException;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudyPageUpdateServiceTest {

    @InjectMocks
    private StudyPageService studyPageService;

    @Mock
    private StudyPageRepository studyPageRepository;

    @Test
    @DisplayName("shouldUpdateStudyPageSuccessfully")
    void shouldUpdateStudyPageSuccessfully() {

        Long studyPageId = 1L;
        UpdateStudyPageRequestDto dto = new UpdateStudyPageRequestDto("New Title", LocalDate.now(), LocalDate.now().plusDays(10), "Updated description");

        StudyPage existingStudyPage = StudyPage.builder()
                .id(studyPageId)
                .studyPageName("Old Title")
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .description("Old description")
                .build();


        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(existingStudyPage));
        when(studyPageRepository.save(any(StudyPage.class))).thenAnswer(invocation -> invocation.<StudyPage>getArgument(0));

        boolean updatedStudyPageSuccessful = studyPageService.updateStudyPage(studyPageId, dto);

        assertThat(updatedStudyPageSuccessful).isTrue();
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNotFound")
    void shouldThrowExceptionWhenStudyPageNotFound() {

        Long studyPageId = 2L;
        UpdateStudyPageRequestDto dto = new UpdateStudyPageRequestDto("New Title", LocalDate.now(), LocalDate.now().plusDays(10),"Updated description");

        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.empty());

        assertThrows(StudyNotFoundException.class, () -> studyPageService.updateStudyPage(studyPageId, dto));
    }
}
