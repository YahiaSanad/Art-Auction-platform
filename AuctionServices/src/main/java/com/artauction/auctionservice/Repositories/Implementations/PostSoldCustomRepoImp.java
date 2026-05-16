package com.artauction.auctionservice.Repositories.Implementations;

import com.artauction.auctionservice.Config.IDs.PostSoldId;
import com.artauction.auctionservice.Entities.PostSold;
import com.artauction.auctionservice.Repositories.Customs.PostSoldCustomRepo;
import com.artauction.auctionservice.Repositories.Interfaces.PostSoldRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostSoldCustomRepoImp implements PostSoldCustomRepo {
    // Attributes
    private final PostSoldRepo postSoldRepo;

    // Methods
    @Override
    @Transactional
    public boolean createPostSold(PostSold postSold) {
        // Check existence
        if (postSoldRepo.existsById(postSold.getId()))
            return false;

        // NOTE: Validate artworkPostId existence
        // NOTE: Validate buyerId existence by calling

        // Save postSold
        postSoldRepo.save(postSold);
        return true;
    }

    @Override
    @Transactional
    public boolean markAsPaid(PostSoldId id) {
        // Check existence
        Optional<PostSold> optional = postSoldRepo.findById(id);
        if (optional.isEmpty())
            return false;

        // Update attribute
        PostSold postSold = optional.get();
        postSold.setPaid(true);
        postSoldRepo.save(postSold);
        return true;
    }
}
