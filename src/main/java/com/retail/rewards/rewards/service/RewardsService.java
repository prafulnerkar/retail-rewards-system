package com.retail.rewards.rewards.service;

import com.retail.rewards.rewards.dto.MonthlyRewardResponse;
import com.retail.rewards.rewards.dto.RewardSummaryResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Reward reporting use cases.
 */
public interface RewardsService {

    /**
     * Calculates total points for a customer over a date range.
     *
     * @param customerId customer identifier
     * @param from start date
     * @param to end date
     * @return total reward summary
     */
    RewardSummaryResponse totalRewards(Long customerId, LocalDate from, LocalDate to);

    /**
     * Returns monthly reward totals for a customer.
     *
     * @param customerId customer identifier
     * @param from start date
     * @param to end date
     * @return list of monthly totals
     */
    List<MonthlyRewardResponse> monthlyRewards(Long customerId, LocalDate from, LocalDate to);
}
