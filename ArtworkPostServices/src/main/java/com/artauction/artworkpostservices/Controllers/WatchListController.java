package com.artauction.artworkpostservices.Controllers;

import com.artauction.artworkpostservices.Aops.Roles.RequiresRole;
import com.artauction.artworkpostservices.Dtos.WatchList.WatchListCreationDto;
import com.artauction.artworkpostservices.Services.Interfaces.WatchListServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/WatchList")
@RequiresRole("Buyer")
@RequiredArgsConstructor
public class WatchListController {
    // Attributes
    private final WatchListServices watchListServices;

    // Get buyer watch list
    @GetMapping("/GetWatchListForBuyer")
    public ResponseEntity<?> getWatchListForBuyer(@RequestParam Integer buyerId) {
        return ResponseEntity.ok(watchListServices.getWatchListForBuyer(buyerId));
    }

    // Create watch artwork post API
    @PostMapping("/CreateWatchList")
    public ResponseEntity<?> createWatchList(@Valid @RequestBody WatchListCreationDto watchListCreationDto) {
        // Create and check result
        boolean result = watchListServices.createWatchList(watchListCreationDto);
        if (!result)
            return ResponseEntity.badRequest()
                    .body("Could not create watchlist relation. Check IDs or duplicate relation.");

        // Return success stats
        return ResponseEntity.ok("Watchlist relation created successfully.");
    }

    // Delete watch artwork post API
    @DeleteMapping("/DeleteWatchList")
    public ResponseEntity<?> deleteWatchList(
            @RequestParam Integer buyerId,
            @RequestParam Integer artworkPostId
    ) {
        // Delete and check result
        boolean result = watchListServices.deleteWatchList(buyerId, artworkPostId);
        if (!result)
            return ResponseEntity.status(404).body("Watchlist relation was not found.");

        // Return success stats
        return ResponseEntity.ok("Watchlist relation deleted successfully.");
    }
}
