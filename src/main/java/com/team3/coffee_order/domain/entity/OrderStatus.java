package com.team3.coffee_order.domain.entity;

public enum OrderStatus {
    ORDERED("주문 완료"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}