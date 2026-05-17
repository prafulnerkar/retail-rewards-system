package com.retail.rewards.rewards.service;

import com.retail.rewards.common.DateRange;
import com.retail.rewards.common.util.DateRangeUtil;
import com.retail.rewards.customer.entity.CustomerEntity;
import com.retail.rewards.customer.repository.CustomerRepository;
import com.retail.rewards.exception.ResourceNotFoundException;
import com.retail.rewards.rewards.dto.MonthlyRewardResponse;
import com.retail.rewards.rewards.dto.RewardSummaryResponse;
import com.retail.rewards.transaction.repository.TransactionRepository;
import com.retail.rewards.security.UserPrincipal;
import com.retail.rewards.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default reward service implementation.
 */
@Service
@RequiredArgsConstructor
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    @Override
    public RewardSummaryResponse totalRewards(Long customerId, LocalDate from, LocalDate to) {
        ensureCustomerExists(customerId);
        ensureAccess(customerId);
        DateRange range = DateRangeUtil.resolveForReport(from, to);
        Long points = transactionRepository.sumRewardPoints(customerId, range.getStart(), range.getEnd());
        return RewardSummaryResponse.builder()
                .customerId(customerId)
                .from(from != null ? from : range.getStart().toLocalDate())
                .to(to != null ? to : range.getEnd().toLocalDate())
                .totalRewardPoints(BigDecimal.valueOf(points != null ? points : 0L))
                .build();
    }

    @Override
    public List<MonthlyRewardResponse> monthlyRewards(Long customerId, LocalDate from, LocalDate to) {
        ensureCustomerExists(customerId);
        ensureAccess(customerId);
        DateRange range = DateRangeUtil.resolveForReport(from, to);

        List<Object[]> rows = transactionRepository.aggregateMonthlyRewardPoints(customerId, range.getStart(), range.getEnd());
        Map<YearMonth, BigDecimal> rewards = new LinkedHashMap<>();
        rows.forEach(row -> {
            YearMonth month = YearMonth.from(((Timestamp) row[0]).toLocalDateTime());
            BigDecimal points = new BigDecimal(String.valueOf(row[1]));
            rewards.put(month, points);
        });

        YearMonth start = YearMonth.from(range.getStart());
        YearMonth end = YearMonth.from(range.getEnd());
        List<MonthlyRewardResponse> result = new ArrayList<>();
        for (YearMonth month = start; !month.isAfter(end); month = month.plusMonths(1)) {
            result.add(MonthlyRewardResponse.builder()
                    .yearMonth(month.toString())
                    .rewardPoints(rewards.getOrDefault(month, BigDecimal.ZERO))
                    .build());
        }
        return result;
    }

    private void ensureCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
    }

    private void ensureAccess(Long customerId) {
        UserPrincipal principal = SecurityUtil.currentPrincipal();
        if (principal.getRoles().contains("ROLE_ADMIN")) {
            return;
        }
        if (principal.getRoles().contains("ROLE_CUSTOMER")) {
            CustomerEntity customer = customerRepository.findByEmail(principal.getEmail())
                    .orElseThrow(() -> new com.retail.rewards.exception.ForbiddenException("Customer profile not linked to authenticated user"));
            if (!customer.getId().equals(customerId)) {
                throw new com.retail.rewards.exception.ForbiddenException("Customers can only access their own reward summary");
            }
            return;
        }
        throw new com.retail.rewards.exception.ForbiddenException("Access denied");
    }
}
