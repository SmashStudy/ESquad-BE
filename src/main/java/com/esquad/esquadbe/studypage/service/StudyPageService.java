package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.BookNotFoundException;
import com.esquad.esquadbe.studypage.exception.BookJsonProcessingException;
import com.esquad.esquadbe.studypage.exception.StudyPageNameNotEqualException;
import com.esquad.esquadbe.studypage.repository.*;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.team.repository.TeamSpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.esquad.esquadbe.studypage.dto.StudyInfoDto.to;

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
    public Long createStudyPage(Long teamId, Long bookId, @Valid StudyInfoDto dto) {
        log.info("Creating StudyPage with teamId: {}, bookId: {}, dto: {}", teamId, bookId, dto);

        // 팀 스페이스 및 도서 조회
        TeamSpace teamSpace = teamSpaceRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);

        StudyPage studyPage = dto.from(teamSpace, book);

        // StudyPage 저장 및 ID 반환
        return studyPageRepository.save(studyPage).getId();
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

        StudyPageReadDto studyPageReadDto = new StudyPageReadDto();
        // DTO 변환 및 반환
        return studyPages.stream()
                .map(studyPageReadDto::from)
                .collect(Collectors.toList());
    }

    // 특정 StudyPage 정보 조회
    public StudyInfoDto readStudyPageInfo(Long id) {
        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No StudyPage found for Study ID: " + id));
        return to(studyPage);
    }

    // StudyPage 업데이트
    public boolean updateStudyPage(Long studyPageId, @Valid UpdateStudyPageRequestDto dto) {
        StudyPage studyPage = studyPageRepository.findById(studyPageId)
                .orElseThrow(BookJsonProcessingException::new);

        studyPage = dto.from(studyPage);

        // StudyPage 저장
        studyPageRepository.save(studyPage);
        return true;
    }

    // StudyPage 삭제
    @Transactional
    public void deleteStudyPage(Long id, String name) {
        // StudyPage 조회
        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(BookJsonProcessingException::new);

        // StudyPage 이름 검증
        if (!studyPage.getStudyPageName().equals(name)) {
            throw new StudyPageNameNotEqualException();
        }

        // 연관된 엔티티 삭제
        studyPageUserRepository.deleteAllByStudyPage(studyPage);
        studyRemindRepository.deleteByStudyPage(studyPage);

        // StudyPage 삭제
        studyPageRepository.delete(studyPage);
    }
}
