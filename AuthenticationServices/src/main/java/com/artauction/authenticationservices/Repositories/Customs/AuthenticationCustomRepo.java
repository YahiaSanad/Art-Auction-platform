package com.artauction.authenticationservices.Repositories.Customs;

public interface AuthenticationCustomRepo {
    boolean revokeRefreshToken(String refreshToken);
}
