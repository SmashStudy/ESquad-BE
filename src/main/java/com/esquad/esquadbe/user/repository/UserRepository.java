package com.esquad.esquadbe.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.esquad.esquadbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends CrudRepository<User, Long> {
   boolean existsByUsername(String username);
   boolean existsByNickname(String nickname);
   Optional<User> findById(long id);
   Optional<User> findByUsername(String username);
   Optional<User> findByNickname(String nickname);
   List<User> findAll();
}
