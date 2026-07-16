package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}