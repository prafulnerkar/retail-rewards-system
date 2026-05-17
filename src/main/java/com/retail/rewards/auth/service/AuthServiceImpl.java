package com.retail.rewards.auth.service;

import com.retail.rewards.auth.dto.AuthResponse;
import com.retail.rewards.auth.dto.LoginRequest;
import com.retail.rewards.auth.dto.RegisterRequest;
import com.retail.rewards.auth.entity.RoleEntity;
import com.retail.rewards.auth.entity.UserEntity;
import com.retail.rewards.auth.repository.RoleRepository;
import com.retail.rewards.auth.repository.UserRepository;
import com.retail.rewards.common.enums.RoleName;
import com.retail.rewards.customer.entity.CustomerEntity;
import com.retail.rewards.customer.repository.CustomerRepository;
import com.retail.rewards.exception.BadRequestException;
import com.retail.rewards.security.JwtTokenProvider;
import com.retail.rewards.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default authentication service implementation.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        validateUniqueEmail(request.getEmail());

        RoleEntity customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new BadRequestException("Customer role is not configured"));

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setFullName((request.getFirstName() + " " + request.getLastName()).trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(customerRole));
        user.setEnabled(true);
        user = userRepository.save(user);

        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(request.getFirstName().trim());
        customer.setLastName(request.getLastName().trim());
        customer.setEmail(request.getEmail().trim().toLowerCase());
        customer.setPhone(request.getPhone());
        customer.setUser(user);
        customerRepository.save(customer);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserPrincipal.from(user), null, UserPrincipal.from(user).getAuthorities());
        String token = jwtTokenProvider.generateToken(authentication);

        return toAuthResponse(user, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtTokenProvider.getExpirationInSecondsValue())
                .userId(principal.getUserId())
                .email(principal.getEmail())
                .fullName(principal.getFullName())
                .roles(principal.getRoles())
                .build();
    }

    private void validateUniqueEmail(String email) {
        String normalized = email.trim().toLowerCase();
        if (userRepository.existsByEmail(normalized) || customerRepository.existsByEmail(normalized)) {
            throw new BadRequestException("Email is already registered");
        }
    }

    private AuthResponse toAuthResponse(UserEntity user, String token) {
        Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtTokenProvider.getExpirationInSecondsValue())
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .build();
    }
}
