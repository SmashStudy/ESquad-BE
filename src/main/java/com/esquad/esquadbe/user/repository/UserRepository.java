package com.esquad.esquadbe.user.repository;

import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    User findByEmail(String email);
    User findByEmailAndUsername(String email, String username);
    Optional<User> findByUsername(String username);
}
