package com.artauction.notificationservices.Kafka;

import com.artauction.notificationservices.Dtos.EmailDto;
import com.artauction.notificationservices.Services.Interfaces.EmailServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class EmailKafkaHandler {
    // Attributes
    private final EmailServices emailServices;

    // Consume email request → send email
    @Bean
    public Consumer<EmailDto> sendEmailIn() {
        // Send email and return result
        return emailRequestDto -> {
            System.out.println("Entered: " + emailRequestDto.getEmail());
            emailServices.sendEmail(new EmailDto(
                    emailRequestDto.getEmail(),
                    emailRequestDto.getSubject(),
                    emailRequestDto.getBody()
            ));
        };
    }
}