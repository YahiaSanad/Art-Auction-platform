package com.artauction.notificationservices.Services.Interfaces;

import com.artauction.notificationservices.Dtos.EmailDto;

public interface EmailServices {
    // Send email for user
    public String sendEmail(EmailDto emailDto);
}
