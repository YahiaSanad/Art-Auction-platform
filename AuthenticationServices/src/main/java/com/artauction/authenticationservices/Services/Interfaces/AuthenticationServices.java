package com.artauction.authenticationservices.Services.Interfaces;

import com.artauction.authenticationservices.Dtos.Authentication.AuthenticationDto;
import com.artauction.authenticationservices.Dtos.Authentication.LoginDto;
import com.artauction.authenticationservices.Dtos.Authentication.RegisterArtistDto;
import com.artauction.authenticationservices.Dtos.Authentication.RegisterBuyerDto;

public interface AuthenticationServices {
    AuthenticationDto registerBuyer(RegisterBuyerDto registerBuyerDto);
    AuthenticationDto registerArtist(RegisterArtistDto registerArtistDto);
    AuthenticationDto login(LoginDto loginDto);
    AuthenticationDto refreshToken(String token);
    boolean logout(String refreshToken);
}
