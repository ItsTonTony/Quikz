package com.echofyteam.backend.feature.auth.service.impl;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * Service for generating and validating JSON Web Tokens (JWT) for authentication.
 * <p>
 * Supports separate handling of access and refresh tokens with distinct secrets and expiration times.
 * </p>
 */
@Service
public class JWTServiceImpl {
    @Value("${app.security.jwt.access-secret}")
    private String accessSecret;

    @Value("${app.security.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${app.security.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${app.security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    /**
     * Enum representing the type of JWT token.
     */
    public enum JwtType {
        ACCESS,
        REFRESH
    }

    /**
     * Generates a JWT token with the specified subject and claims.
     *
     * @param subject the subject (typically username or user ID) of the token
     * @param claims  additional claims to include in the token payload
     * @param jwtType the type of token to generate (ACCESS or REFRESH)
     * @return the generated JWT token as a compact string
     */
    public String generateToken(String subject, Map<String, Object> claims, JwtType jwtType) {
        Instant expiration = LocalDateTime.now()
                .plusSeconds(jwtType == JwtType.ACCESS ? accessExpiration : refreshExpiration)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .expiration(Date.from(expiration))
                .signWith(getSigningKey(jwtType == JwtType.ACCESS ? accessSecret : refreshSecret))
                .compact();
    }

    /**
     * Validates the token by checking if the username matches and the token is not expired.
     *
     * @param token    the JWT token string to validate
     * @param username the username to compare with the token's subject
     * @param jwtType  the type of token (ACCESS or REFRESH)
     * @return true if the token is valid and belongs to the specified username; false otherwise
     */
    public boolean isValid(String token, String username, JwtType jwtType) {
        try {
            String extractedUsername = extractUsername(token, jwtType);
            return extractedUsername.equals(username) && !isExpired(token, jwtType);
        } catch (Exception e) {
            // Optionally log the exception here
            return false;
        }
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token   the JWT token string
     * @param jwtType the type of token (ACCESS or REFRESH)
     * @return the subject (username) contained in the token
     */
    public String extractUsername(String token, JwtType jwtType) {
        return extractAllClaims(token, jwtType).getSubject();
    }

    /**
     * Returns the expiration Instant for a token type relative to the current time.
     *
     * @param jwtType the type of token (ACCESS or REFRESH)
     * @return the expiration Instant of the token
     */
    public Instant getExpirationInstant(JwtType jwtType) {
        long expirationSeconds = jwtType == JwtType.ACCESS ? accessExpiration : refreshExpiration;

        return LocalDateTime.now()
                .plusSeconds(expirationSeconds)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token   the JWT token string
     * @param jwtType the type of token (ACCESS or REFRESH)
     * @return true if the token is expired; false otherwise
     */
    public boolean isExpired(String token, JwtType jwtType) {
        Date expiration = extractAllClaims(token, jwtType).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token   the JWT token string
     * @param jwtType the type of token (ACCESS or REFRESH)
     * @return the Claims object containing all token claims
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    public Claims extractAllClaims(String token, JwtType jwtType) {
        return Jwts.parser()
                .verifyWith(getSigningKey(jwtType == JwtType.ACCESS ? accessSecret : refreshSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Returns the signing key for the given secret.
     *
     * @param secret the Base64-encoded secret key string
     * @return the SecretKey used for signing/verifying JWT tokens
     */
    private SecretKey getSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
