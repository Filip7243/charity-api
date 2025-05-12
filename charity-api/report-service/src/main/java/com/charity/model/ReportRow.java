package com.charity.model;

import java.math.BigDecimal;

public record ReportRow(String name, Integer numOfBoxes, BigDecimal amount, String currencyCode) {

    public ReportRow {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (numOfBoxes == null || numOfBoxes < 0) {
            throw new IllegalArgumentException("Number of boxes cannot be null or negative");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("Currency code cannot be null or blank");
        }
    }
}
