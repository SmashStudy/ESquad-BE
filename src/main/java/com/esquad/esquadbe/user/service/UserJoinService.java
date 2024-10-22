package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.exception.UserNicknameException;
import com.esquad.esquadbe.user.exception.UserUsernameException;

public interface UserJoinService {
    void joinProcess(UserJoinDTO userJoinDTO) throws UserUsernameException, UserNicknameException;

    boolean checkUsername(String username);

    boolean checkNickname(String nickname);
}