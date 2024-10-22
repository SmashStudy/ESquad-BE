package com.esquad.esquadbe.team.exception;

import com.esquad.esquadbe.global.exception.ErrorCode;
import com.esquad.esquadbe.global.exception.RestApiException;

public class InvalidTeamNameException extends RestApiException {

    public InvalidTeamNameException() {
        super(TeamSpaceErrorCode.INVALID_TEAM_NAME_EXCEPTION);
    }
}
