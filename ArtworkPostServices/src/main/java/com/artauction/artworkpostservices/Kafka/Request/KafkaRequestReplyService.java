package com.artauction.artworkpostservices.Kafka.Request;

import com.artauction.artworkpostservices.Dtos.Artist.ArtistDto;
import com.artauction.artworkpostservices.Dtos.PostBid.PostBidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

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
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingCheckAdminReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingCheckBuyerReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingCheckArtistReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<ArtistDto>> pendingArtistDataReplies
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CompletableFuture<List<PostBidDto>>> pendingBidsDataReplies
            = new ConcurrentHashMap<>();

    // Check Admin Existence
    public boolean checkAdminExists(int adminId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingCheckAdminReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(adminId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("checkAdminRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeCheckAdminReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingCheckAdminReplies.remove(correlationId);
        if (future != null)
            future.complete(result);
    }

    // Check Buyer Existence
    public boolean checkBuyerExists(int buyerId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingCheckBuyerReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(buyerId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("checkBuyerRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeCheckBuyerReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingCheckBuyerReplies.remove(correlationId);
        if (future != null)
            future.complete(result);
    }

    // Check Artist Existence
    public boolean checkArtistExists(int artistId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingCheckArtistReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(artistId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("checkArtistRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeCheckArtistReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingCheckArtistReplies.remove(correlationId);
        if (future != null)
            future.complete(result);
    }

    // Fetch Artist Data
    public ArtistDto fetchArtistData(int artistId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<ArtistDto> future = new CompletableFuture<>();
        pendingArtistDataReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(artistId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("artistDataRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeArtistDataReply(String correlationId, ArtistDto dto) {
        CompletableFuture<ArtistDto> future = pendingArtistDataReplies.remove(correlationId);
        if (future != null)
            future.complete(dto);
    }

    // Fetch Bids Data by ArtworkPostId
    public List<PostBidDto> fetchBidsData(int artworkPostId) throws Exception {
        // Create correlationId and register for event
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<List<PostBidDto>> future = new CompletableFuture<>();
        pendingBidsDataReplies.put(correlationId, future);

        // Map message
        Message<Integer> message = MessageBuilder
                .withPayload(artworkPostId)
                .setHeader("correlationId", correlationId)
                .build();

        // Send request
        streamBridge.send("bidsDataRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeBidsDataReply(String correlationId, List<PostBidDto> dtos) {
        CompletableFuture<List<PostBidDto>> future = pendingBidsDataReplies.remove(correlationId);
        if (future != null)
            future.complete(dtos);
    }
}
