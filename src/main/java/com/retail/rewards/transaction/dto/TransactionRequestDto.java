package com.retail.rewards.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    /**
     * Customer ID.
     */
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    /**
     * Transaction amount.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    /**
     * Description.
     */
    private String description;

    /**
     * Transaction date.
     */
    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;
}