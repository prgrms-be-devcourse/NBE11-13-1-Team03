package com.team3.coffee_order.exception;

public class InvalidOrderDateException extends RuntimeException {
    public InvalidOrderDateException(String orderDate) {
        super("날짜 형식이 올바르지 않습니다 (yyyy-MM-dd). orderDate=" + orderDate);
    }
}
