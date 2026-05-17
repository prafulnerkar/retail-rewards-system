package com.retail.rewards.util;

import com.retail.rewards.exception.ForbiddenException;
import com.retail.rewards.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility methods for accessing the security context.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Returns the authenticated user principal.
     *
     * @return current principal
     */
    public static UserPrincipal currentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new ForbiddenException("Authentication context is not available");
        }
        return (UserPrincipal) authentication.getPrincipal();
    }
}
