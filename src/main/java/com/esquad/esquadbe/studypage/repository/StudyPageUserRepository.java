package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.StudyPageUser;
import com.esquad.esquadbe.team.entity.TeamSpace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudyPageUserRepository extends CrudRepository<StudyPageUser, Long> {
    Optional<StudyPageUser> findById(Long userId);
}
