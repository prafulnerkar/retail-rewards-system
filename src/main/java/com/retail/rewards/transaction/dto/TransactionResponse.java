package com.retail.rewards.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction response payload.
 */
@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Long customerId;
    private String transactionReference;
    private BigDecimal amount;
    private Long rewardPoints;
    private LocalDateTime transactionDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
