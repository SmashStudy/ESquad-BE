package com.esquad.esquadbe.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.esquad.esquadbe.team.dto.TeamSpaceResponseDTO;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO getUserProfile(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return UserResponseDTO.builder()
                .username(user.get().getUsername())
                .nickname(user.get().getNickname())
                .email(user.get().getEmail())
                .phoneNumber(user.get().getPhoneNumber())
                .birthDay(user.get().getBirthDay())
                .address(user.get().getAddress())
                .build();
    }

    public Optional<List<TeamSpaceResponseDTO>> getAllTeamSpaces(String userId) {
        Optional<User> user = userRepository.findById(Long.parseLong(userId));
        if(user.isPresent()) {
            List<TeamSpaceResponseDTO> teamSpaceDTOs =
                    user.get().getTeamSpaceUsers().stream()
                            .map(teamSpaceUser -> {
                                TeamSpace teamSpace = teamSpaceUser.getTeamSpace();
                                return TeamSpaceResponseDTO.builder()
                                        .id(teamSpace.getId())
                                        .teamName(teamSpace.getTeamName())
                                        .build();
                            })
                            .toList();
            // return teamSpaceDTOs.isEmpty() ? Optional.of(new ArrayList<>()) : Optional.of(teamSpaceDTOs);
            return Optional.of(teamSpaceDTOs.isEmpty() ? new ArrayList<>() : teamSpaceDTOs);
        } else {
            return Optional.empty();
        }
    }
}
