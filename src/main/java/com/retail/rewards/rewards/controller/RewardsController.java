package com.retail.rewards.rewards.controller;

import com.retail.rewards.common.ApiResponse;
import com.retail.rewards.rewards.dto.MonthlyRewardResponse;
import com.retail.rewards.rewards.dto.RewardSummaryResponse;
import com.retail.rewards.rewards.service.RewardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Reward reporting endpoints.
 */
@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
@Validated
@Tag(name = "Rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    /**
     * Calculates total reward points for a customer and date range.
     *
     * @param customerId customer id
     * @param from optional start date
     * @param to optional end date
     * @return total rewards summary
     */
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "Get total rewards")
    public ApiResponse<RewardSummaryResponse> total(@PathVariable Long customerId,
                                                    @RequestParam(required = false) LocalDate from,
                                                    @RequestParam(required = false) LocalDate to) {
        return ApiResponse.success("Reward summary fetched successfully", rewardsService.totalRewards(customerId, from, to));
    }

    /**
     * Calculates monthly reward points for a customer and date range.
     *
     * @param customerId customer id
     * @param from optional start date
     * @param to optional end date
     * @return monthly reward summary
     */
    @GetMapping("/{customerId}/monthly")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "Get monthly rewards")
    public ApiResponse<List<MonthlyRewardResponse>> monthly(@PathVariable Long customerId,
                                                            @RequestParam(required = false) LocalDate from,
                                                            @RequestParam(required = false) LocalDate to) {
        return ApiResponse.success("Monthly reward summary fetched successfully", rewardsService.monthlyRewards(customerId, from, to));
    }
}
