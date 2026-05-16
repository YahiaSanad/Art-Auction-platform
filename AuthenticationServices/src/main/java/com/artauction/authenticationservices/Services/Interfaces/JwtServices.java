package com.artauction.authenticationservices.Services.Interfaces;

import com.artauction.authenticationservices.Entities.User;

import java.util.Date;

public interface JwtServices {
    String generateJwtToken(User user);
    Date getExpirationDate(String token);
}
