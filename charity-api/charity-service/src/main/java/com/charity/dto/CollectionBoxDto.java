package com.charity.dto;


import com.charity.model.CurrencyCode;

import java.util.Set;

public record CollectionBoxDto(Long boxId, Boolean isAssigned, Boolean isEmpty, Set<CurrencyCode> currencyCodes) {

    public CollectionBoxDto {
        if (currencyCodes == null || currencyCodes.isEmpty()) {
            throw new IllegalArgumentException("Currency codes cannot be null or empty");
        }
        if (isAssigned == null) {
            throw new IllegalArgumentException("Is assigned cannot be null");
        }
        if (isEmpty == null) {
            throw new IllegalArgumentException("Is empty cannot be null");
        }
    }
}
