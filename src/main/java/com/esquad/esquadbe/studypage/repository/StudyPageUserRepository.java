package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyPageUserRepository extends CrudRepository<StudyPageUser, Long> {
    void deleteAllByStudyPage(StudyPage studyPage);
}
