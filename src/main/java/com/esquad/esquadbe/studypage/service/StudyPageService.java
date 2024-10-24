package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.exception.BookNotFoundException;
import com.esquad.esquadbe.studypage.exception.BookJsonProcessingException;
import com.esquad.esquadbe.studypage.exception.StudyNotFoundException;
import com.esquad.esquadbe.studypage.exception.StudyPageNameNotEqualException;
import com.esquad.esquadbe.studypage.repository.*;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.esquad.esquadbe.studypage.dto.StudyInfoDto.to;

@Service
public class StudyPageService {

    private final TeamRepository teamRepository;
    private final StudyPageUserRepository studyPageUserRepository;
    private final BookRepository bookRepository;
    private final StudyPageRepository studyPageRepository;
    private final StudyRemindRepository studyRemindRepository;

    @Autowired
    public StudyPageService(TeamRepository teamRepository,
                            StudyPageUserRepository studyPageUserRepository,
                            BookRepository bookRepository,
                            StudyPageRepository studyPageRepository,
                            StudyRemindRepository studyRemindRepository) {
        this.teamRepository = teamRepository;
        this.studyPageUserRepository = studyPageUserRepository;
        this.bookRepository = bookRepository;
        this.studyPageRepository = studyPageRepository;
        this.studyRemindRepository = studyRemindRepository;
    }

    public Long createStudyPage(Long teamId, Long bookId, @Valid StudyInfoDto dto) {

        TeamSpace teamSpace = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        StudyPage studyPage = dto.from(teamSpace, book);

        return studyPageRepository.save(studyPage).getId();
    }

    public List<StudyPageReadDto> readStudyPages(Long teamId) {

        TeamSpace teamSpace = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);
        List<StudyPage> studyPages = studyPageRepository.findAllByTeamSpace(teamSpace)
                .orElseGet(ArrayList::new);
        StudyPageReadDto studyPageReadDto = new StudyPageReadDto();

        if (studyPages.isEmpty()) {
            return Collections.emptyList();
        }

        return studyPages.stream()
                .map(studyPageReadDto::from)
                .collect(Collectors.toList());
    }

    public StudyInfoDto readStudyPageInfo(Long id) {

        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(StudyNotFoundException::new);

        return to(studyPage);
    }

    public boolean updateStudyPage(Long studyPageId, @Valid UpdateStudyPageRequestDto dto) {

        StudyPage studyPage = studyPageRepository.findById(studyPageId)
                .orElseThrow(StudyNotFoundException::new);
        studyPage = dto.from(studyPage);

        studyPageRepository.save(studyPage);

        return true;
    }

    @Transactional
    public void deleteStudyPage(Long id, String name) {

        StudyPage studyPage = studyPageRepository.findById(id)
                .orElseThrow(BookJsonProcessingException::new);

        if (!studyPage.getStudyPageName().equals(name)) {
            throw new StudyPageNameNotEqualException();
        }

        studyPageUserRepository.deleteAllByStudyPage(studyPage);
        studyRemindRepository.deleteByStudyPage(studyPage);
        studyPageRepository.delete(studyPage);
    }
}
