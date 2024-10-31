package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.team.exception.TeamUserRolePermissionException;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.team.repository.TeamSpaceUserRepository;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import com.esquad.esquadbe.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamSpaceUserServiceImpl implements TeamSpaceUserService {

   private final TeamRepository teamRepository;
   private final UserRepository userRepository;
   private final TeamSpaceUserRepository teamSpaceUserRepository;

   public UserResponseDTO searchUser(final String userId) {
      return userRepository.findById(Long.parseLong(userId))
              .map(UserResponseDTO::from)
              .orElseThrow(UserNotFoundException::new);
   }

   public void checkRole(Long teamId, Long userId) {
      User user = userRepository.findById(userId)
              .orElseThrow(UserNotFoundException::new);
      TeamSpace teamSpace = teamRepository.findById(teamId)
              .orElseThrow(TeamNotFoundException::new);
      Optional<TeamSpaceUser> teamUser = teamSpaceUserRepository.findByMemberAndTeamSpace(Optional.ofNullable(user), Optional.ofNullable(teamSpace));

      teamUser
              .flatMap(teamSpaceUser -> Optional.ofNullable(teamSpaceUser.getRole()))
              .filter("manager"::equals)
              .orElseThrow(TeamUserRolePermissionException::new);
   }

}
