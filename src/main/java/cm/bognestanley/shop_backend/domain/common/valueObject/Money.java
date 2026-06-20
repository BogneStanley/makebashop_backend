package cm.bognestanley.shop_backend.domain.common.valueObject;

import java.math.BigDecimal;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.common.exception.ErrorCode;

public record Money(BigDecimal amount, String currency) {
    

    public Money {
        if (amount == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT,"Amount cannot be null");
        }
        if (currency == null) {
            throw new DomainErrorException(ErrorCode.CURRENCY_CANNOT_BE_NULL,"Currency cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainErrorException(ErrorCode.AMOUNT_CANNOT_BE_NEGATIVE,"Amount cannot be negative");
        }
    }

    public static final Money ZERO = new Money(BigDecimal.ZERO, "FCFA");

    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, currencyCode);
    }

    public Money add(Money other) {
        checkCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        checkCurrency(other);
        BigDecimal newAmount = this.amount.subtract(other.amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainErrorException(ErrorCode.AMOUNT_CANNOT_BE_NEGATIVE,"Resulting amount cannot be negative");
        }
        return new Money(newAmount, this.currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Factor cannot be null");
        }
        BigDecimal newAmount = this.amount.multiply(factor);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainErrorException(ErrorCode.AMOUNT_CANNOT_BE_NEGATIVE, "Resulting amount cannot be negative");
        }
        return new Money(newAmount, this.currency);
    }

    public Money multiply(double factor) {
        return multiply(BigDecimal.valueOf(factor));
    }

    public boolean isGreaterThan(Money other) {
        checkCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        checkCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    private void checkCurrency(Money other) {
        if (other == null) {
            throw new DomainErrorException(ErrorCode.INVALID_INPUT, "Other money cannot be null");
        }
        if (!this.currency.equals(other.currency)) {
            throw new DomainErrorException(ErrorCode.CURRENCY_MISMATCH, "Currencies must match: " + this.currency + " vs " + other.currency);
        }
    }
}
