package com.esquad.esquadbe.team.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class TeamNotFoundException extends RestApiException {

    public TeamNotFoundException() {
        super(TeamSpaceErrorCode.TEAM_NOT_FOUND_EXCEPTION);
    }
}
