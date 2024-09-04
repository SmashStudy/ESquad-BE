package com.esquad.esquadbe.team.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esquad.esquadbe.global.handle.ErrorCode;
import com.esquad.esquadbe.team.service.TeamService;
import com.esquad.esquadbe.user.dto.ResponseDTO;
import com.esquad.esquadbe.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamRestController {

    private final TeamService teamService;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TeamRestController.class);

    @GetMapping("/new")
    public String createTeam() {
        logger.info("createTeam()");
        return "new";
    }

    @GetMapping("/new/{teamName}")
    public ResponseEntity<String> verifyTeamName(@PathVariable String teamName) {
        logger.info("verifyTeamName() ");

        boolean isExistName = teamService.verifyTeamName(teamName);
        if(isExistName) {
            return ResponseEntity.status(ErrorCode.DUPLICATE_NAME.getStatus()).body(ErrorCode.DUPLICATE_NAME.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 팀 스페이스명입니다");
        }
    }

    @PostMapping("/new/search")
    public ResponseEntity<ResponseDTO> searchUser(@RequestBody ResponseDTO dto) {
        logger.info("searchUser()");

        Optional<ResponseDTO> user = userService.searchUserByNick(dto);
        return user.map(responseDTO -> ResponseEntity.status(HttpStatus.OK).body(responseDTO))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).build());
    }


}
