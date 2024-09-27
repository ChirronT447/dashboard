package com.gateway.dashboard.funds.model;

import java.util.List;

/**
 * A fund manager can manage one or more funds
 */
public class FundManager {
    String managerId;
    String managerName;
    List<String> fundsUnderManagement; // fundId
}
