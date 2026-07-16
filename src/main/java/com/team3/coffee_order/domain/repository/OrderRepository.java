package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}