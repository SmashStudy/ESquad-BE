package com.esquad.esquadbe.team.service;

import com.esquad.esquadbe.user.dto.UserResponseDTO;

public interface TeamSpaceUserService {
    UserResponseDTO searchUser(final String username);
    void checkRole(Long teamId, Long userId);
}
