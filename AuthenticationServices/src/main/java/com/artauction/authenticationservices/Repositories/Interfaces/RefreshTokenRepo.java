package com.artauction.authenticationservices.Repositories.Interfaces;

import com.artauction.authenticationservices.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

    // GetAsync → find token and eagerly load User
    @Query("SELECT r FROM RefreshToken r JOIN FETCH r.user WHERE r.token = :token")
    Optional<RefreshToken> findByTokenWithUser(@Param("token") String token);

    // Used in AuthenticationRepository for revoking
    Optional<RefreshToken> findByTokenAndRevokedOnIsNull(String token);
}
