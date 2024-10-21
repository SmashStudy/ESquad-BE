package com.esquad.esquadbe.team.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class DuplicateTeamException extends RestApiException {

    public DuplicateTeamException() {
        super(TeamSpaceErrorCode.TEAM_ALREADY_EXISTS_EXCEPTION);
    }
}
