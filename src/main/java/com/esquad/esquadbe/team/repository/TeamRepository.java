package com.esquad.esquadbe.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esquad.esquadbe.team.entity.TeamSpace;

public interface TeamRepository extends JpaRepository<TeamSpace, Long> {

    boolean existsByTeamName(String name);
}
