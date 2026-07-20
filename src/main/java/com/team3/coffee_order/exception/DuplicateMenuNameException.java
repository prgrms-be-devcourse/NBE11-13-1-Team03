package com.team3.coffee_order.exception;

public class DuplicateMenuNameException extends RuntimeException {
    public DuplicateMenuNameException(String message) {
        super(message);
    }
}
