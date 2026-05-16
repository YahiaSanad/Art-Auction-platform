package com.artauction.auctionservice.Scheduler;

import com.artauction.auctionservice.Kafka.Request.KafkaRequestReplyService;
import com.artauction.auctionservice.Services.Interfaces.AuctionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    // Attributes
    private final AuctionServices auctionServices;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Methods
    @Scheduled(fixedDelay = 60000)
    public void determineWinners() {
        // Get list of ended artwork post IDs from ArtworkPostServices via Kafka
        List<Integer> artworkPostsId = null;
        try {
            artworkPostsId = kafkaRequestReplyService.fetchEndedPostIds();

            // Determine winner
            if (artworkPostsId != null && !artworkPostsId.isEmpty()) {
                artworkPostsId.forEach(auctionServices::determineWinner);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
