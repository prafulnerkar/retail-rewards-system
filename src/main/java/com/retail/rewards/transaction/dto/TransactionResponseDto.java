package com.retail.rewards.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    /**
     * Transaction ID.
     */
    private Long id;

    /**
     * Customer ID.
     */
    private Long customerId;

    /**
     * Transaction amount.
     */
    private BigDecimal amount;

    /**
     * Transaction description.
     */
    private String description;

    /**
     * Transaction date.
     */
    private LocalDateTime transactionDate;

    /**
     * Calculated reward points.
     */
    private Long rewardPoints;
}