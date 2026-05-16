package com.artauction.artistservices.Kafka.Reply;

import com.artauction.artistservices.Dtos.ArtistDto;
import com.artauction.artistservices.Repositories.Interfaces.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ArtistKafkaHandler {
    // Attributes
    private final ArtistRepo artistRepo;

    // Consume artistId → reply boolean (requested by ArtworkPost Service)
    @Bean
    public Function<Message<Integer>, Message<Boolean>> checkArtistRequest() {
        return message -> {
            // Get artistId and correlationId
            int artistId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Check existence
            boolean exists = artistRepo.existsById(artistId);

            // Send reply
            return MessageBuilder
                    .withPayload(exists)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume artistId → reply ArtistDataDto (requested by ArtworkPost Service)
    @Bean
    public Function<Message<Integer>, Message<ArtistDto>> artistDataRequest() {
        return message -> {
            int artistId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            ArtistDto dto = artistRepo.findById(artistId)
                    .map(artist -> new ArtistDto(
                            artist.getId(),
                            artist.getName(),
                            artist.getEmail(),
                            artist.getCountry(),
                            artist.getCity(),
                            artist.getPhoneNumber()
                    ))
                    .orElse(null);

            return MessageBuilder
                    .withPayload(dto)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }
}
