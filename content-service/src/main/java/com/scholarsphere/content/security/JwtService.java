package com.scholarsphere.content.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    /*
     * IMPORTANT:
     * This secret MUST be the same secret used by Auth Service.
     */
    private static final String SECRET_KEY =
            "ScholarSphereSecretKeyForJwtAuthentication2026Secure";

    // ==========================================
    // Signing Key
    // ==========================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ==========================================
    // Extract Email
    // ==========================================

    public String extractEmail(String token) {

        return getClaims(token).getSubject();
    }

    // ==========================================
    // Extract Role
    // ==========================================

    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // ==========================================
    // Extract User ID
    // ==========================================

    public Long extractUserId(String token) {

        Number userId =
                getClaims(token)
                        .get("userId", Number.class);

        return userId.longValue();
    }

    // ==========================================
    // Validate Token
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