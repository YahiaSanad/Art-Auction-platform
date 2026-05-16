package com.artauction.authenticationservices.Dtos.Authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutDto {
    @NotBlank
    private String refreshToken;
}