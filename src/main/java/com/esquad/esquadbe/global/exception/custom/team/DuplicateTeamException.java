package com.esquad.esquadbe.global.exception.custom.team;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.TeamSpaceErrorCode;

public class DuplicateTeamException extends RestApiException {

    public DuplicateTeamException() {
        super(TeamSpaceErrorCode.TEAM_ALREADY_EXISTS_EXCEPTION);
    }
}
