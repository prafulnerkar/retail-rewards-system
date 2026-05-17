package com.retail.rewards.customer.mapper;

import com.retail.rewards.customer.dto.CustomerResponse;
import com.retail.rewards.customer.entity.CustomerEntity;

/**
 * Maps customer entities to response payloads.
 */
public final class CustomerMapper {

    private CustomerMapper() {
    }

    /**
     * Converts a customer entity to a response DTO.
     *
     * @param entity customer entity
     * @return DTO
     */
    public static CustomerResponse toResponse(CustomerEntity entity) {
        Long linkedUserId = entity.getUser() != null ? entity.getUser().getId() : null;
        return CustomerResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .status(entity.getStatus())
                .linkedUserId(linkedUserId)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
