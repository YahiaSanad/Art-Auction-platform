package com.artauction.authenticationservices.Repositories.Interfaces;

import com.artauction.authenticationservices.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepo extends JpaRepository<RefreshToken, Integer> {
    // Find active (not revoked) token
    Optional<RefreshToken> findByTokenAndRevokedOnIsNull(String token);
}
