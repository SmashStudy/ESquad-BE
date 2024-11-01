package com.esquad.esquadbe.team.exception;

import com.esquad.esquadbe.global.exception.RestApiException;

public class TeamUserRolePermissionException extends RestApiException {

    public TeamUserRolePermissionException() {
        super(TeamSpaceUserErrorCode.TEAM_USER_ROLE_PERMIT_EXCEPTION);
    }
}
