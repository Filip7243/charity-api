package com.charity.exception;

public class BoxAlreadyAssignedException extends RuntimeException {
    public BoxAlreadyAssignedException(String message) {
        super(message);
    }
}
