package com.esquad.esquadbe.team.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/team")
public class TeamController {

    private static Logger logger = LoggerFactory.getLogger(TeamController.class);

    @GetMapping("/new")
    public String newTeam() {
        logger.info("newTeam()");

        // 로그인 유효성 검사

        return "/new";
    }



}
