package com.artauction.artistservices.Security;

import com.artauction.artistservices.Config.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtRoleExtractor {
    // Attributes
    private final JwtConfiguration jwtConfiguration;

    public String extractRole(String token) {
        // Build signing key — must match Authentication Service key
        SecretKey signingKey = Keys.hmacShaKeyFor(
                jwtConfiguration.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );

        // Parse token and extract claims
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(jwtConfiguration.getIssuer())
                .requireAudience(jwtConfiguration.getAudience())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Extract roles from claims
        return claims.get("roles", String.class);
    }
}
