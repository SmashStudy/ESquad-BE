package com.esquad.esquadbe.user.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.esquad.esquadbe.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
   boolean existsByUsername(String username);
   boolean existsByNickname(String nickname);
   User findByUsername(String username);
   Optional<User> findByNickname(String nickname);
}