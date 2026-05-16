package com.artauction.auctionservice.Kafka.Request;

import com.artauction.auctionservice.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.auctionservice.Dtos.Buyer.BuyerDto;
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

    // Listen to check buyer reply
    @Bean
    public Consumer<Message<Boolean>> checkBuyerReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeCheckBuyerReply(correlationId, message.getPayload());
        };
    }

    // Listen to check post reply
    @Bean
    public Consumer<Message<Boolean>> checkPostReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeCheckPostReply(correlationId, message.getPayload());
        };
    }

    // Listen to buyer data reply
    @Bean
    public Consumer<Message<BuyerDto>> buyerDataReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeBuyerDataReply(correlationId, message.getPayload());
        };
    }

    // Listen to post data reply
    @Bean
    public Consumer<Message<ArtworkPostDto>> postDataReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completePostDataReply(correlationId, message.getPayload());
        };
    }

    // Listen to ended posts reply
    @Bean
    public Consumer<Message<List<Integer>>> endedPostsReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeEndedPostsReply(correlationId, message.getPayload());
        };
    }

    // Listen to update BuyNowPrice reply
    @Bean
    public Consumer<Message<Boolean>> updateBuyNowPriceReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeUpdateBuyNowPriceReply(correlationId, message.getPayload());
        };
    }

    // Listen to post data reply
    @Bean
    public Consumer<Message<ArtworkPostDto>> endedPostDataReply() {
        return message -> {
            String correlationId = (String) message.getHeaders().get("correlationId");
            kafkaRequestReplyService.completeEndedPostDataReply(correlationId, message.getPayload());
        };
    }
}
