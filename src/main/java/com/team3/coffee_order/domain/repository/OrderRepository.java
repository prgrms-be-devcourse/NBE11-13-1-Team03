package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCustomerAndOrderDate(Customer customer, LocalDate orderDate);
}