package org.example.jwtdemo.repository;

import org.example.jwtdemo.model.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<JpaUser,Long> {
    JpaUser findByUsername(String username);
}
