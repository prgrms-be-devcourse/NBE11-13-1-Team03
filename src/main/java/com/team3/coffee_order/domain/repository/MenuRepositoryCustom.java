package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Menu;

import java.util.Optional;

public interface MenuRepositoryCustom {
    Optional<Menu> findByIdForUpdate(Long id);
}
