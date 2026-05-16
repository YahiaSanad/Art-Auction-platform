package com.artauction.auctionservice.Repositories.Implementations;

import com.artauction.auctionservice.Config.IDs.PostBidId;
import com.artauction.auctionservice.Config.IDs.PostSoldId;
import com.artauction.auctionservice.Entities.PostBid;
import com.artauction.auctionservice.Entities.PostSold;
import com.artauction.auctionservice.Repositories.Customs.PostBidCustomRepo;
import com.artauction.auctionservice.Repositories.Interfaces.PostBidRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostBidCustomRepoImp implements PostBidCustomRepo {
    // Attributes
    private final PostBidRepo postBidRepo;

    // Methods
    @Override
    public Optional<PostSold> getWinner(int artworkPostId) {
        // Get buyer with the highest price
        return postBidRepo.findTopBidByArtworkPostId(artworkPostId)
                .map(topBid -> {
                    PostSold postSold = new PostSold();

                    // Create composite key
                    PostSoldId postSoldId = new PostSoldId();
                    postSoldId.setBuyerId(topBid.getId().getBuyerId());
                    postSoldId.setArtworkPostId(topBid.getId().getArtworkPostId());

                    // Set the composite ID
                    postSold.setId(postSoldId);

                    // Set the final price from the bid
                    postSold.setFinalPrice(topBid.getBuyerPrice());
                    return postSold;
                });
    }

    @Override
    @Transactional
    public boolean createPostBid(PostBid postBid) {
        // Save post bid
        postBidRepo.save(postBid);

        // Update BuyNowPrice update on the artworkPost


        return true;
    }

    @Override
    @Transactional
    public boolean deletePostBid(PostBidId id) {
        // Check existence
        if (!postBidRepo.existsById(id))
            return false;

        // Delete bid
        postBidRepo.deleteById(id);
        return true;
    }
}
