package com.artauction.authenticationservices.Controllers;

import com.artauction.authenticationservices.Dtos.Authentication.LoginDto;
import com.artauction.authenticationservices.Dtos.Authentication.LogoutDto;
import com.artauction.authenticationservices.Dtos.Authentication.RegisterArtistDto;
import com.artauction.authenticationservices.Dtos.Authentication.RegisterBuyerDto;
import com.artauction.authenticationservices.Services.Interfaces.AuthenticationServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    // Attributes
    private final AuthenticationServices authenticationService;

    // Buyer Registration API
    @PostMapping("/BuyerRegistration")
    public ResponseEntity<?> registerBuyer(@RequestBody RegisterBuyerDto registerBuyerDto) {
        // Register buyer and check results
        var authenticationDto = authenticationService.registerBuyer(registerBuyerDto);
        if (!authenticationDto.isAuthenticated())
            return ResponseEntity.badRequest().body(authenticationDto.getMessage());

        // Return token
        return ResponseEntity.ok(authenticationDto);
    }

    // Artist Registration API
    @PostMapping("/ArtistRegistration")
    public ResponseEntity<?> registerArtist(@Valid @RequestBody RegisterArtistDto registerArtistDto) {
        // Register artist and check results
        var authenticationDto = authenticationService.registerArtist(registerArtistDto);
        if (!authenticationDto.isAuthenticated())
            return ResponseEntity.badRequest().body(authenticationDto.getMessage());

        // Return token
        return ResponseEntity.ok(authenticationDto);
    }

    // Login API
    @PostMapping("/Login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        // Login user and check results
        var authenticationDto = authenticationService.login(loginDto);
        if (!authenticationDto.isAuthenticated())
            return ResponseEntity.status(401).body(authenticationDto.getMessage());

        // Return token (with adminId)
        return ResponseEntity.ok(authenticationDto);
    }

    // Refresh token API
    @PostMapping("/RefreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        // Get refresh token and check
        var authenticationDto = authenticationService.refreshToken(refreshToken);
        if (!authenticationDto.isAuthenticated())
            return ResponseEntity.status(401).body(authenticationDto.getMessage());

        // Return token (with adminId)
        return ResponseEntity.ok(authenticationDto);
    }

    // Logout API
    @PostMapping("/Logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutDto logoutDto) {
        // Check result
        var result = authenticationService.logout(logoutDto.getRefreshToken());
        if (!result)
            return ResponseEntity.badRequest().body("Token is invalid, expired, or already revoked.");

        // Return success status
        return ResponseEntity.ok("Successfully logged out.");
    }
}
