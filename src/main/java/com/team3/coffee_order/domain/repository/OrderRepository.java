package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    Optional<Order> findByCustomerAndCreatedAtBetweenAndDeletedFalse(Customer customer, LocalDateTime start, LocalDateTime end);
    Optional<Order> findByIdAndDeletedFalse(Long id);
}
