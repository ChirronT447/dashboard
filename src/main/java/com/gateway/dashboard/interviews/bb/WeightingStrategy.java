package com.gateway.dashboard.interviews.bb;

import java.math.BigDecimal;
import java.util.List;

// --- Core Logic Abstraction (for extensibility) ---
// Using a Strategy pattern to handle different weighting schemes
public interface WeightingStrategy {
    BigDecimal calculate(List<Security> constituents, BigDecimal divisor);
}