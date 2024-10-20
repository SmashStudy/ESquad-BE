package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import com.esquad.esquadbe.team.repository.TeamRepository;
import com.esquad.esquadbe.team.repository.TeamSpaceUserRepository;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamSpaceUserService {

   private final TeamRepository teamRepository;
   private final UserRepository userRepository;
   private final TeamSpaceUserRepository teamSpaceUserRepository;

   public Optional<UserResponseDTO> searchUser(final String username) {
      Optional<User> user = userRepository.findByUsername(username);
      return user.map(UserResponseDTO::from);
   }

   public Optional<String> checkRole(Long teamId, String username) {
      Optional<User> user = userRepository.findByUsername(username);
      Optional<TeamSpace> teamSpace = teamRepository.findById(teamId);
      Optional<TeamSpaceUser> teamUser = teamSpaceUserRepository.findByMemberAndTeamSpace(user, teamSpace);
      return teamUser.map(TeamSpaceUser::getRole);
   }
}
