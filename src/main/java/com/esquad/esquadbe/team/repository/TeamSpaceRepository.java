package com.esquad.esquadbe.team.repository;

import com.esquad.esquadbe.team.entity.TeamSpace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamSpaceRepository extends CrudRepository<TeamSpace, Long> {
}
