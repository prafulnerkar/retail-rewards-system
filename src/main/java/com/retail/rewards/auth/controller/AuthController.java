package com.retail.rewards.auth.controller;

import com.retail.rewards.auth.dto.AuthResponse;
import com.retail.rewards.auth.dto.LoginRequest;
import com.retail.rewards.auth.dto.RegisterRequest;
import com.retail.rewards.auth.service.AuthService;
import com.retail.rewards.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new customer account.
     *
     * @param request registration payload
     * @return auth response with JWT
     */
    @PostMapping("/register")
    @Operation(summary = "Register a customer")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success("Customer registered successfully", authService.register(request));
    }

    /**
     * Authenticates a user and returns a JWT.
     *
     * @param request login payload
     * @return auth response with JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Login a user")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("Login successful", authService.login(request));
    }
}
