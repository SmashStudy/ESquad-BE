package com.esquad.esquadbe.global.exception.custom.team;

import com.esquad.esquadbe.global.exception.custom.RestApiException;
import com.esquad.esquadbe.global.exception.response.TeamSpaceErrorCode;
import com.esquad.esquadbe.global.exception.response.TeamSpaceUserErrorCode;

public class TeamUserRolePermissionException extends RestApiException {

    public TeamUserRolePermissionException() {
        super(TeamSpaceUserErrorCode.TEAM_USER_ROLE_PERMIT_EXCEPTION);
    }
}
