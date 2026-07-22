package com.team3.coffee_order.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.coffee_order.domain.entity.Menu;
import com.team3.coffee_order.domain.entity.QMenu;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<Menu> findByIdForUpdate(Long id) {
        QMenu menu = QMenu.menu;

        Menu result = queryFactory
                .selectFrom(menu)
                .where(menu.id.eq(id))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
