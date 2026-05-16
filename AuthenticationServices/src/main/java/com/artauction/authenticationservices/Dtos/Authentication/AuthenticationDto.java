package com.artauction.authenticationservices.Dtos.Authentication;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthenticationDto {
    // User data
    private int Id;
    private String name;
    private String email;
    private String role;
    private Integer adminId;        // Integer (nullable) instead of int?

    // Authentication data
    private String token;
    private String refreshToken;
    private LocalDateTime expiresOn; // DateTime → LocalDateTime
    private String message;
    private boolean isAuthenticated;
}