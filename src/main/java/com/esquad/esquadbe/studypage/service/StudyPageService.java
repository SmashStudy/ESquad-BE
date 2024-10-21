package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.StudyPageException;
import com.esquad.esquadbe.studypage.repository.*;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.repository.TeamSpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
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
    public StudyPageService(TeamSpaceRepository teamSpaceRepository,
                            StudyPageUserRepository studyPageUserRepository,
                            BookRepository bookRepository,
                            StudyPageRepository studyPageRepository,
                            StudyRemindRepository studyRemindRepository) {
        this.teamSpaceRepository = teamSpaceRepository;
        this.studyPageUserRepository = studyPageUserRepository;
        this.bookRepository = bookRepository;
        this.studyPageRepository = studyPageRepository;
        this.studyRemindRepository = studyRemindRepository;
    }

    // StudyPage 생성
    public Long createStudyPage(Long teamId, Long bookId, StudyInfoDto dto) {
        log.info("Creating StudyPage with teamId: {}, bookId: {}, dto: {}", teamId, bookId, dto);

        validateStudyInfoDto(dto);

        // 팀 스페이스 및 도서 조회
        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(() -> new StudyPageException("Invalid TeamSpace ID: " + teamId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new StudyPageException("Invalid Book ID: " + bookId));

        // StudyPage 객체 생성
        StudyPage studyPage = StudyPage.builder()
                .teamSpace(teamSpace)
                .book(book)
                .studyPageName(dto.getStudyPageName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        // StudyPage 저장 및 ID 반환
        return studyPageRepository.save(studyPage).getId();
    }

    // StudyInfoDto 유효성 검사
    private void validateStudyInfoDto(StudyInfoDto dto) {
        // DTO 검증
        if (dto == null) {
            throw new StudyPageException("StudyInfoDto cannot be null.");
        }
        if (dto.getStudyPageName() == null || dto.getStudyPageName().isEmpty()) {
            throw new StudyPageException("Study Page Name cannot be null or empty.");
        }
        if (dto.getStartDate() == null) {
            throw new StudyPageException("Start Date cannot be null.");
        }
        if (dto.getEndDate() == null) {
            throw new StudyPageException("End Date cannot be null.");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new StudyPageException("End Date cannot be before Start Date.");
        }
    }

    // StudyPage 목록 조회
    public List<StudyPageReadDto> readStudyPages(Long teamId) {
        // 팀 정보 확인
        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamSpace not found with ID: " + teamId));

        // 팀에 속한 StudyPage들 조회
        List<StudyPage> studyPages = studyPageRepository.findAllByTeamSpace(teamSpace)
                .orElseGet(ArrayList::new);

        if (studyPages.isEmpty()) {
            return Collections.emptyList();
        }

        // DTO 변환 및 반환
        return studyPages.stream()
                .map(studyPage -> new StudyPageReadDto(
                        studyPage.getId(),
                        studyPage.getBook().getImgPath(),
                        studyPage.getStudyPageName()
                ))
                .collect(Collectors.toList());
    }

    // 특정 StudyPage 정보 조회
    public StudyInfoDto readStudyPageInfo(Long id) {
        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPage found for Study ID: " + id));
        return new StudyInfoDto(studyPage.getStudyPageName(), studyPage.getStartDate(),
                studyPage.getEndDate(), studyPage.getDescription());
    }

    // StudyPage 업데이트
    public boolean updateStudyPage(Long studyPageId, UpdateStudyPageRequestDto dto) {
        StudyPage studyPage = studyPageRepository.findById(studyPageId)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPage found for Study ID: " + studyPageId));

        // StudyPage 객체 업데이트
        studyPage = StudyPage.builder()
                .id(studyPage.getId())
                .teamSpace(studyPage.getTeamSpace())
                .book(studyPage.getBook())
                .studyPageName(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        // StudyPage 저장
        studyPageRepository.save(studyPage);
        return true;
    }

    // StudyPage 삭제
    @Transactional
    public void deleteStudyPage(Long id, String name) {
        // StudyPage 조회
        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("StudyPage not found with ID: " + id));

        // StudyPage 이름 검증
        if (studyPage.getStudyPageName() == null || !studyPage.getStudyPageName().equals(name)) {
            throw new StudyPageException("StudyPage name does not match or is null for ID: " + studyPage.getId());
        }

        // 연관된 엔티티 삭제
        studyPageUserRepository.deleteAllByStudyPage(studyPage);
        studyRemindRepository.deleteByStudyPage(studyPage);

        // StudyPage 삭제
        studyPageRepository.delete(studyPage);
    }
}
