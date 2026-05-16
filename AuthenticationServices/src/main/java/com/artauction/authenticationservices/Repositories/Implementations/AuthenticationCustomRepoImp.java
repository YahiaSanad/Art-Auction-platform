package com.artauction.authenticationservices.Repositories.Implementations;

import com.artauction.authenticationservices.Entities.RefreshToken;
import com.artauction.authenticationservices.Repositories.Customs.AuthenticationCustomRepo;
import com.artauction.authenticationservices.Repositories.Interfaces.AuthenticationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class AuthenticationCustomRepoImp implements AuthenticationCustomRepo {
    // Attributes
    @Autowired
    private AuthenticationRepo authenticationRepo;

    // Methods
    @Override
    @Transactional
    public boolean revokeRefreshToken(String refreshToken) {
        // Find active token
        Optional<RefreshToken> token = authenticationRepo
                .findByTokenAndRevokedOnIsNull(refreshToken);
        if (token.isEmpty()) return false;

        // Set revoked date and save
        token.get().setRevokedOn(LocalDateTime.now());
        authenticationRepo.save(token.get());

        return true;
    }
}
