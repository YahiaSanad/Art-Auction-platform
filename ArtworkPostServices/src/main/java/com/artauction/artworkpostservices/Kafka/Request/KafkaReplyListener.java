package com.artauction.artworkpostservices.Kafka.Request;

import com.artauction.artworkpostservices.Dtos.Artist.ArtistDto;
import com.artauction.artworkpostservices.Dtos.PostBid.PostBidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class KafkaReplyListener {
    // Attributes
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Listen to check admin reply
    @Bean
    public Consumer<Message<Boolean>> checkAdminReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeCheckAdminReply(correlationId, message.getPayload());
        };
    }

    // Listen to check buyer reply
    @Bean
    public Consumer<Message<Boolean>> checkBuyerReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeCheckBuyerReply(correlationId, message.getPayload());
        };
    }

    // Listen to check artist reply
    @Bean
    public Consumer<Message<Boolean>> checkArtistReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeCheckArtistReply(correlationId, message.getPayload());
        };
    }

    // Listen to artist data reply
    @Bean
    public Consumer<Message<ArtistDto>> artistDataReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeArtistDataReply(correlationId, message.getPayload());
        };
    }

    // Listen to bids data reply
    @Bean
    public Consumer<Message<List<PostBidDto>>> bidsDataReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeBidsDataReply(correlationId, message.getPayload());
        };
    }
}
