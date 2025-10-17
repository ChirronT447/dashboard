package com.gateway.dashboard.interviews.bb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PriceWeightingStrategy implements WeightingStrategy {

    // Define a scale and rounding mode for financial precision
    private static final int CALCULATION_SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    public BigDecimal calculate(List<Security> constituents, BigDecimal divisor) {
        BigDecimal sumOfPrices = constituents.stream()
                .map(Security::getPrice) // Get the price of each security
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum them up, starting from zero

        return sumOfPrices.divide(divisor, CALCULATION_SCALE, ROUNDING_MODE);
    }
}