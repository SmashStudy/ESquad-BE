package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.studypage.entity.StudyRemind;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRemindRepository extends CrudRepository< StudyRemind, Long> {

}
