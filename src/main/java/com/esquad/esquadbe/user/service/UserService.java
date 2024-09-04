package com.esquad.esquadbe.user.service;

import com.esquad.esquadbe.user.dto.ResponseDTO;

import java.util.Optional;

public interface UserService {

   public Optional<ResponseDTO> searchUserByNick(ResponseDTO dto);
}
