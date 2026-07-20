package com.team3.coffee_order.exception;

public class InvalidOrderStatusTransitionException extends RuntimeException {

    public InvalidOrderStatusTransitionException(String message) {
        super(message);
    }
}