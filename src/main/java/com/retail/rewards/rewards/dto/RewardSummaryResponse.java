package com.retail.rewards.rewards.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Total reward summary for a date range.
 */
@Data
@Builder
public class RewardSummaryResponse {
    private Long customerId;
    private LocalDate from;
    private LocalDate to;
    private BigDecimal totalRewardPoints;
}
