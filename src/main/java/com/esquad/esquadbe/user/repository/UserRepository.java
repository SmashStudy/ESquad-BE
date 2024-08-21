package com.esquad.esquadbe.user.repository;

import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);

}
