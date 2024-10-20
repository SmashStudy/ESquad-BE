package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.repository.*;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudyPageService {

    private final TeamSpaceRepository teamSpaceRepository;
    private final StudyPageUserRepository studyPageUserRepository;
    private final BookRepository bookRepository;
    private final StudyPageRepository studyPageRepository;
    private final StudyRemindRepository studyRemindRepository;

    @Autowired
    public StudyPageService(TeamSpaceRepository teamSpaceRepository, StudyPageUserRepository studyPageUserRepository,
                            BookRepository bookRepository, StudyPageRepository studyPageRepository,
                            StudyRemindRepository studyRemindRepository) {
        this.teamSpaceRepository = teamSpaceRepository;
        this.studyPageUserRepository = studyPageUserRepository;
        this.bookRepository = bookRepository;
        this.studyPageRepository = studyPageRepository;
        this.studyRemindRepository = studyRemindRepository;
    }

    // Create
    public Long createStudyPage(Long teamId, Long bookId, StudyInfoDto dto) {
        log.info("Creating StudyPage with teamId: {}, bookId: {}, dto: {}", teamId, bookId, dto);

        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TeamSpace ID"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Book ID"));

        StudyPage studyPage = StudyPage.builder()
                .teamSpace(teamSpace)
                .book(book)
                .studyPageName(dto.getStudyPageName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        studyPageRepository.save(studyPage);
        return studyPage.getId();
    }

    // Read
    public List<StudyPageReadDto> readStudyPages(Long teamId) {
        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamSpace not found with ID: " + teamId));

        List<StudyPage> studyPages = studyPageRepository.findAllByTeamSpace(teamSpace)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPages found for TeamSpace ID: " + teamId));

        return studyPages.stream()
                .map(this::convertStudyPageToStudyPageReadDto)
                .collect(Collectors.toList());
    }

    public StudyInfoDto readStudyPageInfo(Long id) {
        StudyPage studyPage1 = studyPageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPage found for Study ID: " + id));
        return new StudyInfoDto(studyPage1.getStudyPageName(), studyPage1.getStartDate(),studyPage1.getEndDate(), studyPage1.getDescription());
    }

    private StudyPageReadDto convertStudyPageToStudyPageReadDto(StudyPage studyPage) {
        return new StudyPageReadDto(
                studyPage.getId(),
                studyPage.getBook().getImgPath(),
                studyPage.getStudyPageName()
        );
    }

    // Update
    public boolean updateStudyPage(Long studyPageId, UpdateStudyPageRequestDto dto) {
        StudyPage studyPage = studyPageRepository.findById(studyPageId)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPage found for Study ID: " + studyPageId));

        studyPage = StudyPage.builder()
                .id(studyPage.getId())
                .teamSpace(studyPage.getTeamSpace())
                .book(studyPage.getBook())
                .studyPageName(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        studyPageRepository.save(studyPage);
        return true;
    }

    // Delete
    @Transactional
    public void deleteStudyPage(Long id, String name) {
        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StudyPage not found with ID: " + id));

        if (!studyPage.getStudyPageName().equals(name)) {
            throw new IllegalArgumentException("StudyPage name does not match");
        }
        studyPageUserRepository.deleteAllByStudyPage(studyPage);
        studyRemindRepository.deleteByStudyPage(studyPage);
        studyPageRepository.delete(studyPage);
    }
}
