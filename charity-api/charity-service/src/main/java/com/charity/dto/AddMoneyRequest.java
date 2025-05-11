package com.charity.dto;

import com.charity.model.CurrencyCode;
import com.charity.model.Money;

import java.math.BigDecimal;

public record AddMoneyRequest(Long boxId, BigDecimal amount, CurrencyCode code) {

    public AddMoneyRequest {
        if (boxId == null) {
            throw new IllegalArgumentException("Box ID cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (code == null) {
            throw new IllegalArgumentException("Currency code cannot be null");
        }
    }
}
