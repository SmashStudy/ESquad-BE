package com.esquad.esquadbe.team.controller;

import com.esquad.esquadbe.team.dto.TeamSpaceCreateRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceRequestDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceResponseDTO;
import com.esquad.esquadbe.team.dto.TeamSpaceUserResponseDTO;
import com.esquad.esquadbe.team.exception.InvalidTeamNameException;
import com.esquad.esquadbe.user.dto.UserResponseDTO;
import com.esquad.esquadbe.user.service.UserService;
import com.esquad.esquadbe.team.service.TeamSpaceUserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esquad.esquadbe.team.service.TeamService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name="Team", description = "팀 관련")
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamRestController {

    private final TeamService teamService;
    private final TeamSpaceUserServiceImpl teamSpaceUserServiceImpl;
    private final UserService userService;

    @Operation(summary = "소속된 모든 팀스페이스 조회 요청",
            description = "해당 유저가 소속된 모든 팀스페이스를 가져오기 위한 요청")
    @GetMapping
    public Optional<List<TeamSpaceResponseDTO>> getAllTeamSpaces(Principal principal) {
        Optional<List<TeamSpaceResponseDTO>> teamSpaceResponseDTOs = userService.getAllTeamSpaces(principal.getName());
        return teamSpaceResponseDTOs;
    }

    @Operation(summary = "팀스페이스 생성 시, 이름 중복 검사 요청",
            description = "유저가 새로운 팀스페이스를 생성할 때, 이름을 검사하기 위한 요청")
    @GetMapping("/new/{teamName}")
    public ResponseEntity<String> verifyTeamName(@PathVariable String teamName) {
        if (teamName.length() < 2 || teamName.length() > 20)
            throw new InvalidTeamNameException();

        teamService.verifyTeamName(teamName);
        return ResponseEntity.ok("사용 가능한 팀 스페이스명입니다");
    }

    @Operation(summary = "팀스페이스 생성 시, 멤버 조회 요청",
            description = "유저가 새로운 팀스페이스를 생성할 때, 멤버초대를 위한 조회 요청")
    @GetMapping("/new/search/{username}")
    public UserResponseDTO searchUser(@PathVariable("username") String username) {
        return teamSpaceUserServiceImpl.searchUser(username);
    }

    @Operation(summary = "팀스페이스 생성 요청",
            description = "유저가 새로운 팀스페이스를 생성하기 위한 요청")
    @PostMapping("/new")
    public ResponseEntity<String> createTeam(@RequestBody @Valid TeamSpaceCreateRequestDTO teamSpaceRequestDTO) {
        teamService.createTeam(teamSpaceRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("팀 스페이스가 생성되었습니다.");
    }

    @Operation(summary = "팀스페이스 설정 페이지 - 유저 권한 조회 요청",
            description = "유저가 설정 페이지를 클릭했을 때, 권한별 접근을 위해 role 을 조회하는 요청")
    @GetMapping("/settings/{id}")
    public ResponseEntity<Optional<String>> verityUserRole(@PathVariable("id") Long teamId, Principal principal){
        teamSpaceUserServiceImpl.checkRole(teamId, Long.parseLong(principal.getName()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "팀스페이스 설정 - <일반> 프로필 조회 요청",
            description = "유저가 해당 팀스페이스의 프로필을 조회하기 위한 요청")
    @GetMapping("/settings/profile/{id}")
    public TeamSpaceResponseDTO getTeamProfile(@PathVariable("id") Long teamId) {
        return teamService.getTeamProfile(teamId);
    }

    @Operation(summary = "팀스페이스 설정 - <일반> 프로필 수정 요청",
            description = "유저가 해당 팀스페이스의 프로필을 수정하기 위한 요청")
    @PutMapping("/settings/profile")
    public ResponseEntity<TeamSpaceResponseDTO> updateTeamProfile(@RequestBody @Valid TeamSpaceRequestDTO teamSpaceRequestDTO) {
        TeamSpaceResponseDTO responseDTO = teamService.updateProfile(teamSpaceRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "팀스페이스 설정 페이지 - 크루 정보 관리",
            description = "팀에 함께하고 있는 크루 프로필을 보기 위한 요청")
    @GetMapping("/settings/crew/profile/{id}")
    public ResponseEntity<List<TeamSpaceUserResponseDTO>> getCrewProfile(@PathVariable("id") Long teamId, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(teamService.getCrewProfile(teamId, principal.getName()));
    }

    @Operation(summary = "팀스페이스 삭제 처리",
            description = "팀스페이스를 삭제하기 위한 요청")
    @DeleteMapping("/settings/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable("id") Long teamId, Principal principal) {
        teamService.deleteTeamSpace(teamId, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }
}
