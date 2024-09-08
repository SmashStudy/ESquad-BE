package com.esquad.esquadbe.user.repository;

import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    User findByUsername(String username);
    User findById(long id);

}
