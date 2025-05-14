package com.charity.exception;

public class BoxNotEmptyException extends RuntimeException {
    public BoxNotEmptyException(String message) {
        super(message);
    }
}
