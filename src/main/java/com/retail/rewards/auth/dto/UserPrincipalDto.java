package com.retail.rewards.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Lightweight authenticated user details for API responses.
 */
@Data
@Builder
public class UserPrincipalDto {
    private Long userId;
    private String email;
    private String fullName;
    private Set<String> roles;
}
