package com.scholarsphere.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "ScholarSphereSecretKeyForJwtAuthentication2026Secure";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1 hour

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }


    // ==========================================
    // Generate JWT
    // ==========================================

    public String generateToken(
            Long userId,
            String email,
            String role) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + EXPIRATION_TIME
        );

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }


    // ==========================================
    // Extract email from JWT
    // ==========================================

    public String extractEmail(String token) {

        return getClaims(token).getSubject();
    }


    // ==========================================
    // Extract role from JWT
    // ==========================================

    public String extractRole(String token) {

        return getClaims(token).get("role", String.class);
    }


    // ==========================================
    // Extract user ID from JWT
    // ==========================================

    public Long extractUserId(String token) {

        Number userId =
                getClaims(token).get("userId", Number.class);

        return userId.longValue();
    }


    // ==========================================
    // Validate JWT
    // ==========================================

    public boolean isTokenValid(String token) {

        try {

            Claims claims = getClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception e) {

            return false;
        }
    }


    // ==========================================
    // Parse JWT
    // ==========================================

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}