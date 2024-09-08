package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyPageUserService {
    private StudyPageRepository studyPageRepository;
    private UserRepository userRepository;
    private StudyPageUserRepository studyPageUserRepository;

    @Autowired
    public StudyPageUserService(StudyPageRepository studyPageRepository, UserRepository userRepository, StudyPageUserRepository studyPageUserRepository) {
        this.studyPageRepository = studyPageRepository;
        this.userRepository = userRepository;
        this.studyPageUserRepository = studyPageUserRepository;
    }

    public void createStudyPageUser(Long studyPageId, List<Long> ids) {
        //스터디 정보
        StudyPage studyPage = studyPageRepository.findById(studyPageId).orElseThrow(()-> new IllegalArgumentException("Invalid studyPageID"));

        //스터디 멤버 정보
        for(Long userId : ids )  {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid StudyPageUser ID"));
            StudyPageUser studyPageUser = StudyPageUser.builder()
                    .studyPage(studyPage) // 스터디 정보
                    .member(user)
                    .build();

            studyPageUserRepository.save(studyPageUser);
        }
    }
}
