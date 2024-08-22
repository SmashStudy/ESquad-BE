package com.esquad.esquadbe.team.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.team.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamRestController {

    private final TeamService service;

    private static Logger logger = LoggerFactory.getLogger(TeamRestController.class);

    @GetMapping("/new/{teamName}")
    public ResponseEntity<String> checkTeamName(@PathVariable String teamName) {

        logger.info("checkTeamName() ");

        // DB 조회
        boolean isExistName = service.checkTeamName(teamName);
        if(isExistName) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용중인 팀 스페이스명입니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 팀 스페이스명입니다");
    }

}
