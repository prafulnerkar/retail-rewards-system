package com.retail.rewards.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Response returned after login/registration.
 */
@Data
@Builder
public class AuthResponse {
    private String token;
    private String tokenType;
    private Long expiresInSeconds;
    private Long userId;
    private String email;
    private String fullName;
    private Set<String> roles;
}
