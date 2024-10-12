package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudyPageUserRepository extends CrudRepository<StudyPageUser, Long> {
    Optional<StudyPageUser> findByMemberAndStudyPage(User user, StudyPage studyPage);
    void deleteAllByStudyPage(StudyPage studyPage);
}
