package com.artauction.artistservices.Kafka.Request;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

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

    // Check Admin Existence
    public boolean checkAdminExists(int adminId) throws Exception {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingCheckAdminReplies.put(correlationId, future);

        Message<Integer> message = MessageBuilder
                .withPayload(adminId)
                .setHeader("correlationId", correlationId)
                .build();

        streamBridge.send("checkAdminRequest-out-0", message);

        // Wait max 5 seconds for reply
        return future.get(50, TimeUnit.MILLISECONDS);
    }
    public void completeCheckAdminReply(String correlationId, boolean result) {
        CompletableFuture<Boolean> future = pendingCheckAdminReplies.remove(correlationId);
        if (future != null)
            future.complete(result);
    }
}
