package com.artauction.auctionservice.Kafka.Request;

import com.artauction.auctionservice.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.auctionservice.Dtos.ArtworkPost.UpdateBuyNowPriceDto;
import com.artauction.auctionservice.Dtos.Buyer.BuyerDto;
import com.artauction.auctionservice.Dtos.Email.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KafkaRequestReplyService {
    // Attributes
    private final StreamBridge streamBridge;

    // Pending reply maps — correlationId → future
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingBooleanReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<BuyerDto>> pendingBuyerReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<ArtworkPostDto>> pendingPostReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<List<Integer>>> pendingEndedPostsReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingUpdateBuyNowPriceReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<ArtworkPostDto>> pendingEndedPostDataReplies
            = new ConcurrentHashMap<>();

    // Check Buyer Existence
    public boolean checkBuyerExists(Integer buyerId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingBooleanReplies.put(correlationId + ":buyer", future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(buyerId)
                .setHeader("correlationId", correlationId)
                .setHeader("replyType", "checkBuyer")
                .build();

        // Send request
        streamBridge.send("checkBuyerRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeCheckBuyerReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingBooleanReplies.remove(correlationId + ":buyer");
        if (future != null)
            future.complete(result);
    }

    // Check Post Existence
    public boolean checkPostExists(int artworkPostId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingBooleanReplies.put(correlationId + ":post", future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(artworkPostId)
                .setHeader("correlationId", correlationId)
                .setHeader("replyType", "checkPost")
                .build();

        // Send request
        streamBridge.send("checkPostRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeCheckPostReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingBooleanReplies.remove(correlationId + ":post");
        if (future != null)
            future.complete(result);
    }

    // Fetch Buyer Data
    public BuyerDto fetchBuyerData(int buyerId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<BuyerDto> future = new CompletableFuture<>();
        pendingBuyerReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(buyerId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("buyerDataRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeBuyerDataReply(String correlationId, BuyerDto dto) {
        CompletableFuture<BuyerDto> future = pendingBuyerReplies.remove(correlationId);
        if (future != null)
            future.complete(dto);
    }

    // Fetch ArtworkPost Data
    public ArtworkPostDto fetchPostData(int artworkPostId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<ArtworkPostDto> future = new CompletableFuture<>();
        pendingPostReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(artworkPostId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("postDataRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completePostDataReply(String correlationId, ArtworkPostDto dto) {
        CompletableFuture<ArtworkPostDto> future = pendingPostReplies.remove(correlationId);
        if (future != null)
            future.complete(dto);
    }

    // Send Email (Fire-and-Forget)
    public void sendEmail(EmailDto emailDto) {
        // Map message
        Message<EmailDto> message = MessageBuilder
                .withPayload(emailDto)
                .build();

        // Send request
        streamBridge.send("sendEmailOut-out-0", message);
    }

    // Fetch Ended ArtworkPost IDs
    public List<Integer> fetchEndedPostIds() throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<List<Integer>> future = new CompletableFuture<>();
        pendingEndedPostsReplies.put(correlationId, future);

        // Map message
        Message<String> message = MessageBuilder
                .withPayload("fetch")
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("endedPostsRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeEndedPostsReply(String correlationId, List<Integer> postIds) {
        CompletableFuture<List<Integer>> future = pendingEndedPostsReplies.remove(correlationId);
        if (future != null)
            future.complete(postIds);
    }

    // Update BuyNowPrice on ArtworkPost
    public boolean updateBuyNowPrice(int artworkPostId, BigDecimal newBuyNowPrice) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingUpdateBuyNowPriceReplies.put(correlationId, future);

        // Build request DTO
        UpdateBuyNowPriceDto requestDto = new UpdateBuyNowPriceDto(artworkPostId, newBuyNowPrice);

        // Map message
        Message<UpdateBuyNowPriceDto> message = MessageBuilder
                .withPayload(requestDto)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("updateBuyNowPriceRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeUpdateBuyNowPriceReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingUpdateBuyNowPriceReplies.remove(correlationId);
        if (future != null)
            future.complete(result);
    }

    // Fetch ended ArtworkPost Data
    public ArtworkPostDto fetchEndedPostData(int artworkPostId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<ArtworkPostDto> future = new CompletableFuture<>();
        pendingEndedPostDataReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(artworkPostId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("endedPostDataRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeEndedPostDataReply(String correlationId, ArtworkPostDto dto) {
        CompletableFuture<ArtworkPostDto> future = pendingEndedPostDataReplies.remove(correlationId);
        if (future != null)
            future.complete(dto);
    }
}
