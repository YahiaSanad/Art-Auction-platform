package com.artauction.auctionservice.Controllers;

import com.artauction.auctionservice.Aops.Roles.RequiresRole;
import com.artauction.auctionservice.Dtos.PostSold.PostSoldPaidDto;
import com.artauction.auctionservice.Services.Interfaces.PostSoldServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/PostSold")
@RequiredArgsConstructor
public class PostSoldController {
    // Attributes
    private final PostSoldServices postSoldServices;

    // Get post sold API
    @RequiresRole("Admin")
    @GetMapping("/GetAllPostSold")
    public ResponseEntity<?> GetAllPostSold() throws Exception {
        return ResponseEntity.ok(postSoldServices.getAllPostSolds());
    }

    // Get unpaid posts API
    @RequiresRole("Buyer")
    @GetMapping("/GetUnpaidPostForBuyer")
    public ResponseEntity<?> GetUnpaidPostForBuyer(@RequestParam Integer buyerId) {
        return ResponseEntity.ok(postSoldServices.getUnpaidPostSoldForBuyer(buyerId));
    }

    // Get buyer post solds API
    @RequiresRole("Buyer")
    @GetMapping("/GetPostSoldForBuyer")
    public ResponseEntity<?> GetPostSoldForBuyer(@RequestParam Integer buyerId) {
        return ResponseEntity.ok(postSoldServices.getBuyerPostSolds(buyerId));
    }

    // Mark as paid API
    @RequiresRole("Buyer")
    @PatchMapping("/MarkAsPaid")
    public ResponseEntity<?> MarkAsPaid(@RequestBody PostSoldPaidDto postSoldPaidDto) {
        // Check result
        boolean result = postSoldServices.markAsPaid(postSoldPaidDto);
        if (!result)
            return ResponseEntity.badRequest()
                    .body("Failed to mark the post as paid. Verify the Post and Buyer IDs.");

        // Return success
        return ResponseEntity.ok("Successfully marked as paid.");
    }
}
