package com.team3.coffee_order.domain.repository;

import com.team3.coffee_order.domain.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> , MenuRepositoryCustom{

    // ===== 변경 전 (Menu의 @SQLRestriction("deleted = false")이 자동으로 처리해줬음) =====
    // boolean existsByName(String name);
    boolean existsByNameAndDeletedFalse(String name);

    // ===== 변경 전 =====
    // menuRepository.findAll(Sort.by(...)) 를 그대로 사용
    List<Menu> findAllByDeletedFalse(Sort sort);

    // ===== 변경 전 =====
    // menuRepository.findById(menuId) 를 그대로 사용
    Optional<Menu> findByIdAndDeletedFalse(Long id);
}
