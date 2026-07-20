package com.team3.coffee_order.exception;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String status) {
        super("잘못된 주문 상태입니다. status=" + status);
    }
}
