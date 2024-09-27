package com.gateway.dashboard.funds.model;

/**
 * Anything trade-able in a market: Can be merged or split
 * <p>
 * Corporate Action: stock splits, dividend distributions, mergers and acquisitions, rights issues,
 *   Contingent Value Rights (CVRs), spinoffs, name or trading symbol changes, and liquidation.
 */
public class Security {
    String securityId;      // Unique identifier for the security
    String securityName;    // Name of the security
    String securityType;    // Type of security (e.g., stock, bond)
    String ticker;          // Stock/Share ticker symbol if exchange based
    String price;           // Most recent price of the security
    String latestPrice;     // Timestamp of the last price update
}