package com.team3.coffee_order.domain.repository;

import lombok.Getter;

@Getter
public class OrderItemMenuInfoProjection {

    private final Long orderItemId;
    private final Long menuId;
    private final String menuName;

    public OrderItemMenuInfoProjection(Long orderItemId, Long menuId, String menuName) {
        this.orderItemId = orderItemId;
        this.menuId = menuId;
        this.menuName = menuName;
    }
}
