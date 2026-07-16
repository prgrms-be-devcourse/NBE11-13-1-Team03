package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}