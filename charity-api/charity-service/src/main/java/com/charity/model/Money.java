package com.charity.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static jakarta.persistence.EnumType.STRING;
import static java.math.RoundingMode.*;

@Embeddable
public record Money(BigDecimal amount,
                    @Enumerated(STRING)
                    CurrencyCode currencyCode) {

    public Money(BigDecimal amount, CurrencyCode currencyCode) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currencyCode == null) {
            throw new IllegalArgumentException("Currency code cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = setScale(amount);
        this.currencyCode = currencyCode;
    }

    public Money add(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Cannot add money with different currency codes");
        }
        // TODO: moze tutaj sprwadzac czy 0 jest oddawane i nic nie robic jesli tak
        return new Money(this.amount.add(other.amount), this.currencyCode);
    }

    public Money add(BigDecimal amount) {
        // TODO: moze tutaj sprwadzac czy 0 jest oddawane i nic nie robic jesli tak
        return new Money(this.amount.add(amount), this.currencyCode);
    }

    public Money subtract(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Cannot subtract money with different currency codes");
        }
        return new Money(this.amount.subtract(other.amount), this.currencyCode);
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

    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, HALF_EVEN);
    }
}
