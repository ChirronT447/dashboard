package com.gateway.dashboard.interviews.bb;

import java.math.BigDecimal;

// --- Data Models (My 'Domain') ---
public class Security {

    private final String ticker;
    private final BigDecimal price;
    private final long sharesOutstanding;

    public Security(String ticker, BigDecimal price, long sharesOutstanding) {
        this.ticker = ticker;
        this.price = price;
        this.sharesOutstanding = sharesOutstanding;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getSharesOutstanding() {
        return sharesOutstanding;
    }
}