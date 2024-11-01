package com.esquad.esquadbe.team.repository;

import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamSpaceUserRepository extends JpaRepository<TeamSpaceUser, Long> {

    Optional<TeamSpaceUser> findByMemberAndTeamSpace(Optional<User> user, Optional<TeamSpace> teamSpace);

    List<TeamSpaceUser> findAllByTeamSpace(TeamSpace teamSpace);
}
