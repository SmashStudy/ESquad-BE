package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.global.exception.custom.user.UserUsernameException;
import com.esquad.esquadbe.global.exception.custom.user.UserNicknameException;

public interface UserJoinService {
    void joinProcess(UserJoinDTO userJoinDTO) throws UserUsernameException, UserNicknameException;

    boolean checkUsername(String username);

    boolean checkNickname(String nickname);
}