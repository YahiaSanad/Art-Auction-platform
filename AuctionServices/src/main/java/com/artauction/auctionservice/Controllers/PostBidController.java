package com.artauction.auctionservice.Controllers;

import com.artauction.auctionservice.Aops.Roles.RequiresRole;
import com.artauction.auctionservice.Dtos.PostBid.PostBidCreationDto;
import com.artauction.auctionservice.Services.Interfaces.PostBidServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/PostBid")
@RequiredArgsConstructor
public class PostBidController {
    // Attributes
    private final PostBidServices postBidServices;

    // Get post bids API
    @GetMapping("/GetAllPostBids")
    public ResponseEntity<?> GetAllPostBids(@RequestParam Integer artworkPostId) {
        return ResponseEntity.ok(postBidServices.getAllPostBids(artworkPostId));
    }

    // Get buyer post bids API
    @RequiresRole("Buyer")
    @GetMapping("/GetBuyerPostBids")
    public ResponseEntity<?> GetBuyerPostBids(@RequestParam Integer buyerId) {
        return ResponseEntity.ok(postBidServices.getAllBuyerBids(buyerId));
    }

    // Create post bid API
    @RequiresRole("Buyer")
    @PostMapping("/CreatePostBid")
    public ResponseEntity<?> CreatePostBid(@RequestBody PostBidCreationDto postBidCreationDto) {
        // Check result
        boolean result = postBidServices.createPostBid(postBidCreationDto);
        if (!result)
            return ResponseEntity.badRequest()
                    .body("Could not place bid. Ensure the post is still active.");

        // Return success
        return ResponseEntity.ok("Bid placed successfully. Wait for final results to appear.");
    }

    // Delete post bid API
    @RequiresRole("Buyer")
    @DeleteMapping("/DeletePostBid")
    public ResponseEntity<?> DeletePostBid(@RequestParam Integer artworkPostId,
                                           @RequestParam Integer buyerId) {
        // Check result
        boolean result = postBidServices.deletePostBid(artworkPostId, buyerId);
        if (!result)
            return ResponseEntity.notFound().build();

        // Return success
        return ResponseEntity.ok("Bid deleted successfully.");
    }
}
