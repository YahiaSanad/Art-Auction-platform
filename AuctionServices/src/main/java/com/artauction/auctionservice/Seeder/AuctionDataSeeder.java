package com.artauction.auctionservice.Seeder;

import com.artauction.auctionservice.Entities.PostBid;
import com.artauction.auctionservice.Repositories.Interfaces.PostBidRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuctionDataSeeder implements CommandLineRunner {
    // Attributes
    private final PostBidRepo postBidRepo;

    @Override
    public void run(String... args) throws Exception {
        /* if (postBidRepo.count() > 0) return;

        // Seed 3 Bids for each of the 10 artworks
        for (int postId = 1; postId <= 10; postId++) {
            for (int b = 1; b <= 3; b++) {
                PostBid bid = new PostBid();
                // Set Composite ID
                bid.getId().setArtworkPostId(postId);
                bid.getId().setBuyerId(7 + b); // Using Buyer IDs 8, 9, 10

                bid.setBuyerPrice(BigDecimal.valueOf(150.00 * postId + (b * 50)));
                bid.setBidTime(LocalDateTime.now().plusHours(b));

                postBidRepo.save(bid);
            }
        }
        System.out.println(">> AuctionService: Seeded 30 Bids (3 for each post)."); */
    }
}
