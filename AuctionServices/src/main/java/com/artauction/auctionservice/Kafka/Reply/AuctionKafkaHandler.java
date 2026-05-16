package com.artauction.auctionservice.Kafka.Reply;

import com.artauction.auctionservice.Dtos.PostBid.PostBidDto;
import com.artauction.auctionservice.Mappers.PostBid.PostBidMapper;
import com.artauction.auctionservice.Repositories.Customs.PostBidCustomRepo;
import com.artauction.auctionservice.Repositories.Interfaces.PostBidRepo;
import com.artauction.auctionservice.Services.Interfaces.PostBidServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuctionKafkaHandler {
    // Attributes
    private final PostBidServices postBidServices;
    private final PostBidMapper postBidMapper;

    // Consume artworkPostId → reply List<BidDataDto> (requested by ArtworkPost Service)
    @Bean
    public Function<Message<Integer>, Message<List<PostBidDto>>> bidsDataFromPostRequest() {
        return message -> {
            // Get artworkPostId and correlation ID
            int artworkPostId = message.getPayload();
            String correlationId = (String) message.getHeaders().get("correlationId");

            // Fetch all bids for this artwork post
            List<PostBidDto> dtos = postBidServices.getAllPostBids(artworkPostId);

            return MessageBuilder
                    .withPayload(dtos)
                    .setHeader("correlationId", correlationId)
                    .build();
        };
    }
}
