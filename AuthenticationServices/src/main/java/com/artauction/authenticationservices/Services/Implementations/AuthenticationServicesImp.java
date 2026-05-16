package com.artauction.authenticationservices.Services.Implementations;

import com.artauction.authenticationservices.Dtos.Authentication.AuthenticationDto;
import com.artauction.authenticationservices.Dtos.Authentication.LoginDto;
import com.artauction.authenticationservices.Dtos.Authentication.RegisterArtistDto;
import com.artauction.authenticationservices.Dtos.Authentication.RegisterBuyerDto;
import com.artauction.authenticationservices.Entities.Artist;
import com.artauction.authenticationservices.Entities.Buyer;
import com.artauction.authenticationservices.Entities.RefreshToken;
import com.artauction.authenticationservices.Entities.User;
import com.artauction.authenticationservices.Mappers.AuthenticationMapper;
import com.artauction.authenticationservices.Mappers.RegisterArtistMapper;
import com.artauction.authenticationservices.Mappers.RegisterBuyerMapper;
import com.artauction.authenticationservices.Repositories.Customs.AuthenticationCustomRepo;
import com.artauction.authenticationservices.Repositories.Interfaces.RefreshTokenRepo;
import com.artauction.authenticationservices.Repositories.Interfaces.UserRepo;
import com.artauction.authenticationservices.Services.Interfaces.AuthenticationServices;
import com.artauction.authenticationservices.Services.Interfaces.JwtServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServicesImp implements AuthenticationServices {
    // Attributes
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final AuthenticationCustomRepo authenticationCustomRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtServices;
    private final AuthenticationMapper authenticationMapper;
    private final RegisterArtistMapper registerArtistMapper;
    private final RegisterBuyerMapper registerBuyerMapper;

    // Methods
    private <T extends User> AuthenticationDto register(String email, String password, String role, T user) {
        // Local authentication DTO
        AuthenticationDto authenticationDto = new AuthenticationDto();

        // Check email existence
        if (userRepo.findByEmail(email).isPresent()) {
            authenticationDto.setMessage("Email is already registered");
            return authenticationDto;
        }

        // Hash password and set it
        user.setPassword(passwordEncoder.encode(password));

        // Save user
        userRepo.save(user);

        // Generate JWT token
        String jwtToken = jwtServices.generateJwtToken(user);

        // Generate and save refresh token
        RefreshToken refreshToken = generateRefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepo.save(refreshToken);

        // Map and fill authentication DTO
        authenticationDto = authenticationMapper.toAuthenticationDto(user);
        authenticationDto.setRole(role);
        authenticationDto.setToken(jwtToken);
        authenticationDto.setExpiresOn(LocalDateTime.ofInstant(
                jwtServices.getExpirationDate(jwtToken).toInstant(), ZoneId.systemDefault()));
        authenticationDto.setAuthenticated(true);
        authenticationDto.setRefreshToken(refreshToken.getToken());

        return authenticationDto;
    }

    @Override
    public AuthenticationDto registerBuyer(RegisterBuyerDto registerBuyerDto) {
        Buyer buyer = registerBuyerMapper.toBuyer(registerBuyerDto);
        return register(registerBuyerDto.getEmail(), registerBuyerDto.getPassword(), "Buyer", buyer);
    }

    @Override
    public AuthenticationDto registerArtist(RegisterArtistDto registerArtistDto) {
        Artist artist = registerArtistMapper.toArtist(registerArtistDto);
        return register(registerArtistDto.getEmail(), registerArtistDto.getPassword(), "Artist", artist);
    }

    @Override
    public AuthenticationDto login(LoginDto loginDto) {
        // Local authentication DTO
        AuthenticationDto authenticationDto = new AuthenticationDto();

        // Check email existence and password
        Optional<User> optionalUser = userRepo.findByEmail(loginDto.getEmail());
        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(loginDto.getPassword(), optionalUser.get().getPassword())) {
            authenticationDto.setMessage("Username or password is not correct");
            return authenticationDto;
        }
        User user = optionalUser.get();

        // Generate JWT token
        String jwtToken = jwtServices.generateJwtToken(user);

        // Generate and save refresh token
        RefreshToken refreshToken = generateRefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepo.save(refreshToken);

        // Map and fill authentication DTO
        authenticationDto = authenticationMapper.toAuthenticationDto(user);
        authenticationDto.setRole(user.getRole());
        authenticationDto.setToken(jwtToken);
        authenticationDto.setExpiresOn(LocalDateTime.ofInstant(
                jwtServices.getExpirationDate(jwtToken).toInstant(), ZoneId.systemDefault()));
        authenticationDto.setAuthenticated(true);
        authenticationDto.setRefreshToken(refreshToken.getToken());

        // Add admin ID if user is Artist
        if (user instanceof Artist artist) {
            Integer adminId = (artist.getAdmin() != null) ? artist.getAdmin().getId() : null;
            authenticationDto.setAdminId(adminId);
        }

        return authenticationDto;
    }

    @Override
    public AuthenticationDto refreshToken(String token) {
        // Local authentication DTO
        AuthenticationDto authenticationDto = new AuthenticationDto();

        // Get and validate refresh token
        Optional<RefreshToken> storedToken = refreshTokenRepo.findByTokenAndRevokedOnIsNull(token);
        if (storedToken.isEmpty() || !storedToken.get().isActive()) {
            authenticationDto.setMessage("Invalid refresh token");
            return authenticationDto;
        }

        User user = storedToken.get().getUser();

        // Revoke old token (rotation)
        storedToken.get().setRevokedOn(LocalDateTime.now());
        refreshTokenRepo.save(storedToken.get());

        // Generate new JWT token
        String jwtToken = jwtServices.generateJwtToken(user);

        // Generate and save new refresh token
        RefreshToken newRefreshToken = generateRefreshToken();
        newRefreshToken.setUser(user);
        refreshTokenRepo.save(newRefreshToken);

        // Map and fill authentication DTO
        authenticationDto = authenticationMapper.toAuthenticationDto(user);
        authenticationDto.setRole(user.getRole());
        authenticationDto.setToken(jwtToken);
        authenticationDto.setExpiresOn(LocalDateTime.ofInstant(
                jwtServices.getExpirationDate(jwtToken).toInstant(), ZoneId.systemDefault()));
        authenticationDto.setAuthenticated(true);
        authenticationDto.setRefreshToken(newRefreshToken.getToken());

        // Add admin ID if user is Artist
        if (user instanceof Artist artist) {
            Integer adminId = (artist.getAdmin() != null) ? artist.getAdmin().getId() : null;
            authenticationDto.setAdminId(adminId);
        }

        return authenticationDto;
    }

    @Override
    public boolean logout(String refreshToken) {
        // Check empty string
        if (refreshToken == null || refreshToken.isBlank())
            return false;

        // Revoke refresh token
        return authenticationCustomRepo.revokeRefreshToken(refreshToken);
    }

    private RefreshToken generateRefreshToken() {
        // Generate 64 random bytes
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);

        return RefreshToken.builder()
                .token(Base64.getEncoder().encodeToString(randomBytes))
                .createdOn(LocalDateTime.now())
                .expiresOn(LocalDateTime.now().plusDays(30))
                .build();
    }
}
