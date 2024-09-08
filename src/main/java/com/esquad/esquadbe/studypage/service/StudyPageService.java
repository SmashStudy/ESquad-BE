package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageDto;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.studypage.entity.StudyRemind;
import com.esquad.esquadbe.studypage.repository.*;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyPageService {
    
    private final TeamSpaceRepository teamSpaceRepository;
    private final UserRepository userRepository;
    private final StudyPageUserRepository studyPageUserRepository;
    private final BookRepository bookRepository;
    private final StudyRemindRepository studyRemindRepository;
    private final StudyPageRepository studyPageRepository;

    @Autowired
    public StudyPageService(TeamSpaceRepository teamSpaceRepository, UserRepository userRepository, StudyPageUserRepository studyPageUserRepository, BookRepository bookRepository, StudyRemindRepository studyRemindRepository, StudyPageRepository studyPageRepository) {
        this.teamSpaceRepository = teamSpaceRepository;
        this.userRepository = userRepository;
        this.studyPageUserRepository = studyPageUserRepository;
        this.bookRepository = bookRepository;
        this.studyRemindRepository = studyRemindRepository;
        this.studyPageRepository = studyPageRepository;
    }

    // Create
    public Long createStudyPage(Long teamId, Long bookId, StudyInfoDto dto) {

        // 팀, 책 정보
        TeamSpace teamSpace = teamSpaceRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Invalid TeamSpace ID"));
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new IllegalArgumentException("Invalid Book ID"));

        // 스터디 정보
        StudyPage studyPage = StudyPage.builder()
                .teamSpace(teamSpace) // 팀 정보
                .book(book) // 책 정보
                .studyPageName(dto.getStudyPageName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .build();

        studyPageRepository.save(studyPage);
        return studyPage.getId();
    }

    // Update
    public void updateStudyPage(Long id, StudyPageDto dto) {}

    //Delete
    public void deleteStudyPage(Long id, String name) {}
}
