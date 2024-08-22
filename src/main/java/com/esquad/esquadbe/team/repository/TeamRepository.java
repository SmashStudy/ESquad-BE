package com.esquad.esquadbe.team.repository;

import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamSpace, Long> {

    boolean existsByTeamName(String name);
}
