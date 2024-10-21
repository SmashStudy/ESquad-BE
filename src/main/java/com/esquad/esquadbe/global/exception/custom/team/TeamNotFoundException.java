package com.esquad.esquadbe.global.exception.custom.team;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.TeamSpaceErrorCode;

public class TeamNotFoundException extends RestApiException {

    public TeamNotFoundException() {
        super(TeamSpaceErrorCode.TEAM_NOT_FOUND_EXCEPTION);
    }
}
