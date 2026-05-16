package com.artauction.authenticationservices.Dtos.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterBuyerDto {
    @NotBlank
    @Length(max = 60)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(max = 20)
    private String password;

    @NotBlank
    @Length(max = 50)
    private String city;

    @NotBlank
    @Length(max = 50)
    private String country;

    @NotBlank
    @Length(max = 13)
    private String phoneNumber;

    @NotBlank
    @Length(max = 150)
    private String address;
}
