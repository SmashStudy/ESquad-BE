package com.esquad.esquadbe.team.repository;

import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.annotation.Nullable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TeamRepository extends CrudRepository<TeamSpace, Long> {

    @Nullable
    Optional<TeamSpace> findByTeamName(String name);
}
