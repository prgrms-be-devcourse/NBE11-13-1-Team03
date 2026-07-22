package com.team3.coffee_order.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.coffee_order.domain.entity.QMenu;
import com.team3.coffee_order.domain.entity.QOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private static final QOrderItem orderItem = QOrderItem.orderItem;
    private static final QMenu menu = QMenu.menu;

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Long> findOrderIdsByMenuNameIncludingDeleted(String menuName) {
        return queryFactory
                .select(orderItem.order.id).distinct()
                .from(orderItem)
                .join(orderItem.menu, menu)
                .where(menu.name.eq(menuName))
                .fetch();
    }

    @Override
    public List<OrderItemMenuInfoProjection> findMenuInfosByOrderItemIds(List<Long> orderItemIds) {
        return queryFactory
                .select(Projections.constructor(OrderItemMenuInfoProjection.class,
                        orderItem.id, menu.id, menu.name))
                .from(orderItem)
                .join(orderItem.menu, menu)
                .where(orderItem.id.in(orderItemIds))
                .fetch();
    }
}
