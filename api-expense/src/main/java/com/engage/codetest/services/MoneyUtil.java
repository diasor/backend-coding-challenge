package com.engage.codetest.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;

public final class MoneyUtil {
    private MoneyUtil() {}

    public static BigDecimal roundAmountToCurrency(BigDecimal amount, Currency currency) {
        BigDecimal rounded = amount.round(new MathContext(currency.getDefaultFractionDigits(), RoundingMode.HALF_DOWN));
        return rounded.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_DOWN);
    }
}
