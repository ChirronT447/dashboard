package com.gateway.dashboard.interviews.bb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MarketCapWeightingStrategy implements WeightingStrategy {

    private static final int CALCULATION_SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    public BigDecimal calculate(List<Security> constituents, BigDecimal divisor) {
        // Market Cap = Price * SharesOutstanding
        BigDecimal totalMarketCap = constituents.stream()
                .map(s -> s.getPrice().multiply(BigDecimal.valueOf(s.getSharesOutstanding())))
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum the individual market caps

        return totalMarketCap.divide(divisor, CALCULATION_SCALE, ROUNDING_MODE);
    }
}