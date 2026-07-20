package com.team3.coffee_order.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("이메일은 비어 있을 수 없습니다.");
    }
}
