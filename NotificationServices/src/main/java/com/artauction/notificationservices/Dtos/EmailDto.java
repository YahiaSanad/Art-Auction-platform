package com.artauction.notificationservices.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    @Email
    private String email;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

    public EmailDto(@Email String email, @NotBlank String subject, @NotBlank String body) {
        this.email = email;
        this.subject = subject;
        this.body = body;
    }
}
