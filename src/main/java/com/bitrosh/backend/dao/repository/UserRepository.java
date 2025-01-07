package com.bitrosh.backend.dao.repository;

import java.util.Optional;

import com.bitrosh.backend.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    long countByRole(User.Role role);
}
