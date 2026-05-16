package com.artauction.authenticationservices.Services.Implementations;

import com.artauction.authenticationservices.Config.JwtConfiguration;
import com.artauction.authenticationservices.Entities.User;
import com.artauction.authenticationservices.Services.Interfaces.JwtServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServicesImp implements JwtServices {
    // Attributes
    private final JwtConfiguration jwtConfiguration;
    private final UserDetailsService userDetailsService;

    // Generate JWT Token
    @Override
    public String generateJwtToken(User user) {
        // Build claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("email", user.getEmail());
        claims.put("uid", user.getId().toString());

        // Add roles to claims
        String roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", roles);

        // Get signing key
        Key signingKey = Keys.hmacShaKeyFor(
                jwtConfiguration.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );

        // Build and return token
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtConfiguration.getIssuer())
                .setAudience(jwtConfiguration.getAudience())
                .setExpiration(new Date(System.currentTimeMillis() +
                        (long) jwtConfiguration.getExpiryHours() * 60 * 60 * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Date getExpirationDate(String token) {
        SecretKey signingKey = Keys.hmacShaKeyFor(
                jwtConfiguration.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
