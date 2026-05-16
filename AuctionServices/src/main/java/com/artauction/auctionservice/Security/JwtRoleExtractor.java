package com.artauction.auctionservice.Security;

import com.artauction.auctionservice.Config.Security.JwtConfiguration;
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

    private final JwtConfiguration jwtConfiguration;

    public String extractRole(String token) {
        SecretKey signingKey = Keys.hmacShaKeyFor(
                jwtConfiguration.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(jwtConfiguration.getIssuer())
                .requireAudience(jwtConfiguration.getAudience())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("roles", String.class);
    }
}
