package com.artauction.authenticationservices.Dtos.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(max = 20)
    private String password;
}