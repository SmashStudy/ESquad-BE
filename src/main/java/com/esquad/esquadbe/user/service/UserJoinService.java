package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.UserJoinDTO;
import com.esquad.esquadbe.user.exception.UserIdException;
import com.esquad.esquadbe.user.exception.UserNicknameException;

public interface UserJoinService {
    void joinProcess(UserJoinDTO userJoinDTO) throws UserIdException, UserNicknameException;

    boolean checkUsername(String username);

    boolean checkNickname(String nickname);
}
