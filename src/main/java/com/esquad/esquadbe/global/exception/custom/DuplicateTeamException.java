package com.esquad.esquadbe.global.exception.custom;

import com.esquad.esquadbe.global.exception.response.TeamSpaceErrorCode;

public class DuplicateTeamException extends DuplicateException {

    public DuplicateTeamException() {
        super(TeamSpaceErrorCode.TEAM_ALREADY_EXISTS_EXCEPTION);
    }
}
