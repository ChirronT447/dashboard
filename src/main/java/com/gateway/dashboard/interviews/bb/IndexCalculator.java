package com.gateway.dashboard.interviews.bb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class IndexCalculator {

    private final WeightingStrategy strategy;

    public IndexCalculator(WeightingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "Strategy cannot be null.");
    }

    public BigDecimal calculateIndexValue(List<Security> constituents, BigDecimal divisor) {
        // Basic error handling for input
        if (constituents == null || constituents.isEmpty()) {
            return BigDecimal.ZERO;
        }
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Divisor must be a positive number.");
        }
        return strategy.calculate(constituents, divisor);
    }
}