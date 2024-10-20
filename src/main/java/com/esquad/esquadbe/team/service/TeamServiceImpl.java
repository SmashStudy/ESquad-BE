package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.global.exception.custom.BusinessBaseException;
import com.esquad.esquadbe.global.exception.custom.DuplicateTeamException;
import com.esquad.esquadbe.global.exception.response.CommonErrorCode;
import com.esquad.esquadbe.notification.entity.NotificationType;
import com.esquad.esquadbe.notification.service.NotificationService;
import com.esquad.esquadbe.team.dto.TeamSpaceCreateRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceResponseDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceUserResponseDTO;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.entity.TeamSpaceUser;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.esquad.esquadbe.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final NotificationService notificationService;

    @Override
    public void verifyTeamName(String name) {
        if(!teamRepository.existsByTeamName(name)) {
            throw new DuplicateTeamException();
        }
    }

    @Override
    @Transactional
    public TeamSpace createTeam(TeamSpaceCreateRequestDTO teamDTO) {
        TeamSpace teamSpace = teamDTO.to();
        teamSpace.joinMembers();
        log.info("teamSpace: {}", teamSpace);

        try {
            // 스페이스가 저장될 때 스페이스 유저 또한 함께 저장되어야 함 : CASCADE:PERSIST
            TeamSpace saved = teamRepository.save(teamSpace);

            // 알림 전송 (추후 EventListener 로 역할 분리)
            saved.getMembers()
                    .stream()
                    .forEach(member -> notificationService.send(member.getMember()
                            , "[ " + saved.getTeamName() + "] 의 크루로 초대되었습니다"
                            , NotificationType.JOIN));
            return saved;
        } catch (BusinessBaseException e) {
            throw new BusinessBaseException(e.getMessage(), CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<TeamSpaceResponseDTO> getTeamProfile(Long id) {
        return teamRepository.findById(id)
                .map(TeamSpaceResponseDTO::from); // Optional 에서 안전하게 변환
    }

    @Override
    @Transactional
    public void deleteTeamSpace(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    @Override
    @Transactional
    public TeamSpaceResponseDTO updateProfile(@Valid TeamSpaceRequestDTO teamSpaceRequestDTO) {
        return TeamSpaceResponseDTO.from(teamRepository.save(teamSpaceRequestDTO.to()));
    }

    @Override
    public List<TeamSpaceUserResponseDTO> getCrewProfile(Long teamId) {
         List<TeamSpaceUserResponseDTO> crewProfiles = new ArrayList<>();
         Optional<TeamSpace> teamSpace = teamRepository.findById(teamId);
         if(teamSpace.isPresent()) {
             // Optional<List<TeamSpaceUser>> teamSpaceUsers = teamSpaceUserRepository.findAllByTeamSpace(teamSpace);
             List<TeamSpaceUser> teamSpaceUsers = teamSpace.get().getMembers();

             teamSpaceUsers
                     .stream()
                     .map(teamUser -> crewProfiles.add(TeamSpaceUserResponseDTO.from(teamUser)))
                     .toList();
         }
         return crewProfiles;
    }

    @Override
    public List<TeamSpaceUserResponseDTO> getCrewRole(Long teamId) {

        return List.of();
    }



}
