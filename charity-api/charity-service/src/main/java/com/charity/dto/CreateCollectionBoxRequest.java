package com.charity.dto;

import com.charity.model.CurrencyCode;

import java.util.HashSet;
import java.util.Set;

public record CreateCollectionBoxRequest(Set<CurrencyCode> currencyCodes,
                                         Long fundraisingEventId) {

    public CreateCollectionBoxRequest {
        if (currencyCodes == null || currencyCodes.isEmpty()) {
            throw new IllegalArgumentException("Currency codes cannot be null or empty");
        }
    }
}
