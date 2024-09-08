package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyPageRepository  extends CrudRepository< StudyPage, Long> {
  StudyPage findStudyPageById(Long id);
}
