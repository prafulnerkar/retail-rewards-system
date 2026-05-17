package com.retail.rewards.security;

import com.retail.rewards.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates and validates JWT tokens.
 */
@Component
public class JwtTokenProvider {

    private final String secret;
    private final long expirationInSeconds;

    @Getter
    private Key signingKey;

    public JwtTokenProvider(@Value("${security.jwt.secret}") String secret,
                            @Value("${security.jwt.expiration-in-seconds}") long expirationInSeconds) {
        this.secret = secret;
        this.expirationInSeconds = expirationInSeconds;
    }

    @PostConstruct
    public void init() {
        byte[] decoded = java.util.Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.signingKey = Keys.hmacShaKeyFor(decoded);
    }

    /**
     * Generates a signed JWT for the current authentication.
     *
     * @param authentication authentication object
     * @return compact JWT string
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationInSeconds * 1000L);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("userId", principal.getUserId())
                .claim("fullName", principal.getFullName())
                .claim("roles", principal.getRoles())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a token and returns its claims.
     *
     * @param token JWT string
     * @return claims
     */
    public Claims parseClaims(String token) {
        try {
            Jws<Claims> parsed = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return parsed.getBody();
        } catch (Exception ex) {
            throw new InvalidTokenException("Invalid or expired JWT token");
        }
    }

    /**
     * Validates the token signature and expiration.
     *
     * @param token JWT string
     * @return true when valid
     */
    public boolean validateToken(String token) {
        parseClaims(token);
        return true;
    }

    /**
     * Returns the configured token lifetime.
     *
     * @return expiration seconds
     */
    public long getExpirationInSecondsValue() {
        return expirationInSeconds;
    }
}
