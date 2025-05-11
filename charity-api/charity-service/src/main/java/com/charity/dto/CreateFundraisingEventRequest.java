package com.charity.dto;

import com.charity.model.CurrencyCode;

public record CreateFundraisingEventRequest(String name,
                                            CurrencyCode code) {

    public CreateFundraisingEventRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (code == null) {
            throw new IllegalArgumentException("Currency code cannot be null");
        }
    }
}
