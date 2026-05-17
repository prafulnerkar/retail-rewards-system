package com.retail.rewards.transaction.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request payload for recording a transaction.
 */
@Data
public class TransactionCreateRequest {

    /**
     * Optional customer identifier. If omitted by a customer principal, the current customer's profile is used.
     */
    private Long customerId;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    @Size(max = 500)
    private String description;
}
