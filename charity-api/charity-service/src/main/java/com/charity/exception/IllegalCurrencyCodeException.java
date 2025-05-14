package com.charity.exception;

public class IllegalCurrencyCodeException extends RuntimeException {
    public IllegalCurrencyCodeException(String message) {
        super(message);
    }
}
