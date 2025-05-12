package com.charity.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CurrencyCode {
    USD, EUR, PLN, GBP;

    public static CurrencyCode convertTo(String value) {
        try {
            return CurrencyCode.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            log.error("Invalid currency code: {}", value);
            throw new IllegalArgumentException("Invalid currency code: " + value);
        }
    }
}
