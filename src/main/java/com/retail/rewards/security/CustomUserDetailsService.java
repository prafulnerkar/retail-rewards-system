package com.retail.rewards.security;

import com.retail.rewards.auth.entity.UserEntity;
import com.retail.rewards.auth.repository.UserRepository;
import com.retail.rewards.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Loads user accounts for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by username, which is the email address.
     *
     * @param username email address
     * @return user principal
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        return UserPrincipal.from(user);
    }
}
