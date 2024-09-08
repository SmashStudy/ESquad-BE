package com.esquad.esquadbe.studypage.repository;

import com.esquad.esquadbe.team.entity.TeamSpace;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TeamSpaceRepository extends CrudRepository<TeamSpace, Long> {

    Optional<TeamSpace> findById(Long teamId);
}
