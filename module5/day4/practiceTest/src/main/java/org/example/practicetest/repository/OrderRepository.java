package org.example.practicetest.repository;

import org.example.practicetest.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);
}

