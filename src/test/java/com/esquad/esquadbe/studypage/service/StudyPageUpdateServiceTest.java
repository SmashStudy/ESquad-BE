package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import jakarta.persistence.EntityNotFoundException;
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
        // Given
        Long studyPageId = 1L;
        UpdateStudyPageRequestDto dto = new UpdateStudyPageRequestDto("New Title", LocalDate.now(), LocalDate.now().plusDays(10), "Updated description");

        StudyPage existingStudyPage = StudyPage.builder()
                .id(studyPageId)
                .studyPageName("Old Title")
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(5))
                .description("Old description")
                .build();

        // Mocking repository behavior
        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.of(existingStudyPage));
        when(studyPageRepository.save(any(StudyPage.class))).thenAnswer(invocation -> {
            return invocation.<StudyPage>getArgument(0); // 업데이트된 객체 반환
        });

        // When
        boolean updatedStudyPageSuccessful = studyPageService.updateStudyPage(studyPageId, dto);

        // Then
        assertThat(updatedStudyPageSuccessful).isTrue(); // 업데이트 성공 여부 확인
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenStudyPageNotFound")
    void shouldThrowExceptionWhenStudyPageNotFound() {
        // Given
        Long studyPageId = 2L;
        UpdateStudyPageRequestDto dto = new UpdateStudyPageRequestDto("New Title", LocalDate.now(), LocalDate.now().plusDays(10),"Updated description");

        when(studyPageRepository.findById(studyPageId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            studyPageService.updateStudyPage(studyPageId, dto);
        });
    }
}
