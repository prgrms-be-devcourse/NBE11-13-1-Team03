package com.team3.coffee_order.domain.entity;

public enum OrderStatus {
    ORDERED("주문 완료"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 주문 상태 변경 규칙
    public boolean canChangeTo(OrderStatus nextStatus){
        return switch (this){
            case ORDERED -> nextStatus == SHIPPING || nextStatus == CANCELED;
            case SHIPPING -> nextStatus == DELIVERED;
            case DELIVERED, CANCELED -> false;
        };
    }

}
