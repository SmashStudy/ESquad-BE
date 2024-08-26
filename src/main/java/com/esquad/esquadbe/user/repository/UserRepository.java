package com.esquad.esquadbe.user.repository;

import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);

}
