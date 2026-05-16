package com.artauction.authenticationservices.Kafka;

import com.artauction.authenticationservices.Dtos.Buyer.BuyerDto;
import com.artauction.authenticationservices.Repositories.Interfaces.AdminRepo;
import com.artauction.authenticationservices.Repositories.Interfaces.BuyerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthenticationKafkaHandler {
    // Attributes
    private final BuyerRepo buyerRepo;
    private final AdminRepo adminRepo;

    // Consume adminId → reply boolean (requested by ArtworkPost Service)
    @Bean
    public Function<Message<Integer>, Message<Boolean>> checkAdminRequest() {
        return message -> {
            int adminId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            boolean exists = adminRepo.existsById(adminId);

            return MessageBuilder
                    .withPayload(exists)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume buyerId → reply boolean (requested by ArtworkPost Service)
    @Bean
    public Function<Message<Integer>, Message<Boolean>> checkBuyerFromPostRequest() {
        return message -> {
            int buyerId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            boolean exists = buyerRepo.existsById(buyerId);

            return MessageBuilder
                    .withPayload(exists)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume buyerId → reply boolean existence
    @Bean
    public Function<Message<Integer>, Message<Boolean>> checkBuyerRequest() {
        return message -> {
            // Get buyerId and correlationId
            int buyerId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Check buyer existence
            boolean exists = buyerRepo.existsById(buyerId);

            // Send reply
            return MessageBuilder
                    .withPayload(exists)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume buyerId → reply BuyerDataDto
    @Bean
    public Function<Message<Integer>, Message<BuyerDto>> buyerDataRequest() {
        return message -> {
            // Get buyerId and CorrelationId
            int buyerId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Map Buyer entity to BuyerDto
            BuyerDto dto = buyerRepo.findById(buyerId)
                    .map(buyer -> {
                        return new BuyerDto(buyer.getName(), buyer.getEmail());
                    })
                    .orElseGet(() -> {
                        return null;
                    });

            // Send reply
            return MessageBuilder
                    .withPayload(dto)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume adminId → reply boolean (requested by Artist Service)
    @Bean
    public Function<Message<Integer>, Message<Boolean>> checkAdminFromArtistRequest() {
        return message -> {
            // Get adminId and CorrelationId
            int adminId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Check existence
            boolean exists = adminRepo.existsById(adminId);

            // Send reply
            return MessageBuilder
                    .withPayload(exists)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }
}
