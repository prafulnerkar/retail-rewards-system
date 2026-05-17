package com.retail.rewards.customer.dto;

import com.retail.rewards.common.enums.CustomerStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Customer response payload.
 */
@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerStatus status;
    private Long linkedUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
