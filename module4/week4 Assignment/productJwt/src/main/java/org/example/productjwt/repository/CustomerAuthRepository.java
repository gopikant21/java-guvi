package org.example.productjwt.repository;

import org.example.productjwt.model.CustomerAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAuthRepository extends JpaRepository<CustomerAuth, Long> {
    Optional<CustomerAuth> findByUsername(String username);
    Optional<CustomerAuth> findByCustomerId(Long customerId);
}

