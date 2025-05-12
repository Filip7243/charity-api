package com.charity.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateResponse(String base, String date, Map<String, BigDecimal> rates) {

    public ExchangeRateResponse {
        if (base == null || base.isBlank()) {
            throw new IllegalArgumentException("Base currency cannot be null or blank");
        }
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date cannot be null or blank");
        }
        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Rates cannot be null or empty");
        }
    }
}
