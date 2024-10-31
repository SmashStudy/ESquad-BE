package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.global.exception.RestApiException;
import com.esquad.esquadbe.team.exception.DuplicateTeamException;
import com.esquad.esquadbe.team.exception.TeamNotFoundException;
import com.esquad.esquadbe.global.exception.CommonErrorCode;
import com.esquad.esquadbe.notification.entity.NotificationType;
import com.esquad.esquadbe.notification.service.NotificationService;
import com.esquad.esquadbe.team.dto.TeamSpaceCreateRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceResponseDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceUserResponseDTO;
import com.esquad.esquadbe.team.entity.TeamSpace;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.esquad.esquadbe.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamSpaceUserService teamSpaceUserService;
    private final NotificationService notificationService;

    @Override
    public void verifyTeamName(String name) {
        if(teamRepository.existsByTeamName(name)) {
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
                    .forEach(
                            member -> notificationService.send(member.getMember()
                            , saved.getMembers().stream().findFirst().map(manager -> manager.getMember().getUsername()) +
                                    "님이 [ " + saved.getTeamName() + " ] 의 크루로 초대했습니다!"
                            , NotificationType.JOIN));
            return saved;
        } catch (RestApiException e) {
            throw new RestApiException(e.getMessage(), CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public TeamSpaceResponseDTO getTeamProfile(Long id) {
        return teamRepository.findById(id)
                .map(TeamSpaceResponseDTO::from)
                .orElseThrow(TeamNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteTeamSpace(Long teamId, String userId) {
        teamSpaceUserService.checkRole(teamId, Long.parseLong(userId));
        teamRepository.deleteById(teamId);
    }

    @Override
    @Transactional
    public TeamSpaceResponseDTO updateProfile(TeamSpaceRequestDTO teamSpaceRequestDTO) {
        return TeamSpaceResponseDTO.from(teamRepository.save(teamSpaceRequestDTO.to()));
    }

    @Override
    public List<TeamSpaceUserResponseDTO> getCrewProfile(Long teamId, String userId) {
        teamSpaceUserService.checkRole(teamId, Long.parseLong(userId));
        return teamRepository.findById(teamId)
                .map(TeamSpace::getMembers)
                .orElseThrow(TeamNotFoundException::new)
                .stream()
                .map(TeamSpaceUserResponseDTO::from)
                .toList();
    }
}
