package com.artauction.artworkpostservices.Kafka.Reply;

import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.UpdateBuyNowPriceDto;
import com.artauction.artworkpostservices.Mappers.ArtworkPost.ArtworkPostMapper;
import com.artauction.artworkpostservices.Repositories.Interfaces.ArtworkPostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ArtworkPostKafkaHandler {
    // Attributes
    private final ArtworkPostRepo artworkPostRepo;
    private final ArtworkPostMapper artworkPostMapper;

    // Consume artworkPostId → reply boolean existence
    @Bean
    public Function<Message<Integer>, Message<Boolean>> checkPostRequest() {
        return message -> {
            // Get artworkPostId and correlationId
            Integer artworkPostId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Check post existence
            boolean exists = artworkPostRepo.existsByIdAndAdminIdIsNotNullAndEndDateAfter(artworkPostId, LocalDateTime.now());

            // Send reply
            return MessageBuilder
                    .withPayload(exists)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume artworkPostId → reply ArtworkPostDataDto
    @Bean
    public Function<Message<Integer>, Message<ArtworkPostDto>> postDataRequest() {
        return message -> {
            // Get artworkPostId and correlationId
            int artworkPostId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Map ArtworkPost entity to ArtworkPostDataDto
            ArtworkPostDto artworkPostDto = artworkPostRepo.findApprovedAndActiveById(artworkPostId, LocalDateTime.now())
                    .map(artworkPostMapper::toDto)
                    .orElse(new ArtworkPostDto());

            // Send reply
            return MessageBuilder
                    .withPayload(artworkPostDto)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume ended artwork post IDs
    @Bean
    public Function<Message<String>, Message<List<Integer>>> endedPostsRequest() {
        return message -> {
            // Get correlation ID
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Fetch all artwork post IDs where endDate has passed and post is approved
            List<Integer> endedPostIds = artworkPostRepo.findEndedPostIds(LocalDateTime.now());

            // Send reply
            return MessageBuilder
                    .withPayload(endedPostIds)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume  update price and reply boolean
    @Bean
    public Function<Message<UpdateBuyNowPriceDto>, Message<Boolean>> updateBuyNowPriceRequest() {
        return message -> {
            // Get correlation ID and message
            String correlationId = (String) message.getHeaders().get("correlationId");
            UpdateBuyNowPriceDto requestDto = message.getPayload();

            // Find artwork post and update BuyNowPrice
            boolean updated = artworkPostRepo.findById(requestDto.getArtworkPostId())
                    .map(post -> {
                        post.setBuyNowPrice(requestDto.getBuyNowPrice());
                        artworkPostRepo.save(post);
                        return true;
                    })
                    .orElse(false);

            // Send reply
            return MessageBuilder
                    .withPayload(updated)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }

    // Consume artworkPostId → reply ArtworkPostDataDto
    @Bean
    public Function<Message<Integer>, Message<ArtworkPostDto>> endedPostDataRequest() {
        return message -> {
            // Get artworkPostId and correlationId
            int artworkPostId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Map ArtworkPost entity to ArtworkPostDataDto
            ArtworkPostDto artworkPostDto = artworkPostRepo.findEndedPost(artworkPostId, LocalDateTime.now())
                    .map(artworkPostMapper::toDto)
                    .orElse(null);

            // Send reply
            return MessageBuilder
                    .withPayload(artworkPostDto)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }
}