package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository;
import com.esquad.esquadbe.studypage.exception.StudyNotFoundException;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.team.repository.TeamSpaceUserRepository;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.user.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyPageUserService {
    private final StudyPageRepository studyPageRepository;
    private final StudyPageUserRepository studyPageUserRepository;
    private final TeamRepository teamRepository;
    private final TeamSpaceUserRepository teamSpaceUserRepository;
    @Autowired
    public StudyPageUserService(StudyPageRepository studyPageRepository, TeamSpaceUserRepository teamSpaceUserRepository, StudyPageUserRepository studyPageUserRepository, TeamRepository teamRepository) {
        this.studyPageRepository = studyPageRepository;
        this.teamSpaceUserRepository = teamSpaceUserRepository;
        this.studyPageUserRepository = studyPageUserRepository;
        this.teamRepository = teamRepository;
    }

    public void createStudyPageUser(Long teamId, @NotNull Long studyPageId, List<Long> ids) {
        TeamSpace teamSpace = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        StudyPage studyPage = studyPageRepository.findById(studyPageId).orElseThrow(StudyNotFoundException::new);

        List<TeamSpaceUser> allTeamUser = teamSpaceUserRepository.findAllByTeamSpace(teamSpace);
        List<User> allUser = new ArrayList<>();

        for (TeamSpaceUser teamUser : allTeamUser) {
            User user = teamUser.getMember();
            if (user!=null){
                allUser.add(user);
            }
        }

        for (User user : allUser) {
            StudyPageUser studyPageUser = StudyPageUser.builder()
                    .studyPage(studyPage)
                    .member(user)
                    .build();

            studyPageUserRepository.save(studyPageUser);
        }
    }
}
