package com.gateway.dashboard.funds.model;

import java.util.List;

/**
 *
 */
public class Fund {
    String fundId;
    String fundName;
    String fundType;
    String inceptionDate;
    String fundManagerId;

    List<FundHolding> fundHoldings; // A fund will have many holdings
}
