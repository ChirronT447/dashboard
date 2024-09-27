package com.gateway.dashboard.funds.model;

/**
 * Each fund will have a number of holdings which are a quantity of a Security purchased for a price on a date.
 */
public class FundHolding {
    String holdingId;       // Unique identifier for the holding
    String fundId;          // ID of the associated fund
    String securityId;      // ID of the security held
    String quantity;        // Number of securities held
    String purchasePrice;   // Price at which the security was purchased
    String purchaseDate;    // Date of purchase
}
