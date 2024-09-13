package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudyPageUserService {
    private final StudyPageRepository studyPageRepository;
    private final UserRepository userRepository;
    private final StudyPageUserRepository studyPageUserRepository;

    @Autowired
    public StudyPageUserService(StudyPageRepository studyPageRepository, UserRepository userRepository, StudyPageUserRepository studyPageUserRepository) {
        this.studyPageRepository = studyPageRepository;
        this.userRepository = userRepository;
        this.studyPageUserRepository = studyPageUserRepository;
    }

    public void createStudyPageUser(Long studyPageId, List<Long> ids) {
        //스터디 정보
        StudyPage studyPage = studyPageRepository.findById(studyPageId).orElseThrow(()-> new IllegalArgumentException("Invalid studyPageID"));
        log.info(String.valueOf(ids.get(0)));

        // 1차 기능 - 모든 유저가 사용할 수 있음 : 팀 전체 -> 개별 설정
        Iterable<User> allUsers = userRepository.findAll();

        //스터디 유저에 정보 저장
        for (User user : allUsers) {
            StudyPageUser studyPageUser = StudyPageUser.builder()
                    .studyPage(studyPage) // 스터디 정보
                    .member(user)
                    .build();

            studyPageUserRepository.save(studyPageUser);
        }
    }
}
