package com.retail.rewards.rewards.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Monthly reward summary for a customer.
 */
@Data
@Builder
public class MonthlyRewardResponse {
    private String yearMonth;
    private BigDecimal rewardPoints;
}
