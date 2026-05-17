package com.retail.rewards.auth.service;

import com.retail.rewards.auth.dto.AuthResponse;
import com.retail.rewards.auth.dto.LoginRequest;
import com.retail.rewards.auth.dto.RegisterRequest;

/**
 * Authentication and registration use cases.
 */
public interface AuthService {

    /**
     * Registers a customer and creates a login account.
     *
     * @param request registration payload
     * @return auth response including JWT
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates an existing user and issues a JWT.
     *
     * @param request login payload
     * @return auth response including JWT
     */
    AuthResponse login(LoginRequest request);
}
