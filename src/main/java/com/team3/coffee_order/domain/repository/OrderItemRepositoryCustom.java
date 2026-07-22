package com.team3.coffee_order.domain.repository;

import java.util.List;

public interface OrderItemRepositoryCustom {
    List<Long> findOrderIdsByMenuNameIncludingDeleted(String menuName);
    List<OrderItemMenuInfoProjection> findMenuInfosByOrderItemIds(List<Long> orderItemIds);
}
