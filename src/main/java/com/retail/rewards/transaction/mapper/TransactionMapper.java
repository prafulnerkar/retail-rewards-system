package com.retail.rewards.transaction.mapper;

import com.retail.rewards.transaction.dto.TransactionResponse;
import com.retail.rewards.transaction.entity.CustomerTransactionEntity;

/**
 * Maps transaction entities to response payloads.
 */
public final class TransactionMapper {

    private TransactionMapper() {
    }

    /**
     * Converts a transaction entity to a response DTO.
     *
     * @param entity transaction entity
     * @return DTO
     */
    public static TransactionResponse toResponse(CustomerTransactionEntity entity) {
        return TransactionResponse.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer().getId())
                .transactionReference(entity.getTransactionReference())
                .amount(entity.getAmount())
                .rewardPoints(entity.getRewardPoints())
                .transactionDate(entity.getTransactionDate())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
