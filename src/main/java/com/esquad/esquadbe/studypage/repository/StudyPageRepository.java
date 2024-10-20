package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.team.entity.TeamSpace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyPageRepository  extends CrudRepository< StudyPage, Long> {
  Optional<List<StudyPage>> findAllByTeamSpace(TeamSpace teamSpace);

  Optional<StudyPage> findById(Long id);
}
