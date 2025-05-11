package com.charity.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.*;

@Embeddable
public record Money(BigDecimal amount, CurrencyCode currencyCode) {

    public Money(BigDecimal amount, CurrencyCode currencyCode) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currencyCode == null) {
            throw new IllegalArgumentException("Currency code cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative nor zero");
        }

        this.amount = setScale(amount);
        this.currencyCode = currencyCode;
    }

    public Money add(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Cannot add money with different currency codes");
        }
        return new Money(this.amount.add(other.amount), this.currencyCode);
    }

    public Money subtract(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Cannot subtract money with different currency codes");
        }
        return new Money(this.amount.subtract(other.amount), this.currencyCode);
    }

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, HALF_EVEN);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;

        if (!amount.equals(money.amount)) return false;
        return currencyCode == money.currencyCode;
    }

    public int hashCode() {
        int result = amount.hashCode();
        result = 31 * result + currencyCode.hashCode();
        return result;
    }
}
