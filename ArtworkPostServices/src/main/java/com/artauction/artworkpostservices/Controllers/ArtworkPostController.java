package com.artauction.artworkpostservices.Controllers;

import com.artauction.artworkpostservices.Aops.Roles.RequiresRole;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostCreationDto;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostUpdatingDto;
import com.artauction.artworkpostservices.Services.Interfaces.ArtworkPostServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/ArtworkPost")
@RequiredArgsConstructor
public class ArtworkPostController {
    // Attributes
    private final ArtworkPostServices artworkPostServices;

    // Get all artwork post API
    @GetMapping("/GetAllArtworkPosts")
    public ResponseEntity<?> getAllArtworkPosts() {
        return ResponseEntity.ok(artworkPostServices.getAllArtworkPosts());
    }

    // Get all artist artwork post API
    @RequiresRole({"Artist", "Admin"})
    @GetMapping("/GetAllArtistPosts")
    public ResponseEntity<?> getAllArtistPosts(@RequestParam Integer artistId) {
        return ResponseEntity.ok(artworkPostServices.getAllArtistArtworkPosts(artistId));
    }

    // Get detailed artwork post API
    @GetMapping("/GetPostWithDetails")
    public ResponseEntity<?> getPostWithDetails(@RequestParam Integer artworkPostId) {
        return ResponseEntity.ok(artworkPostServices.getArtworkPostDetails(artworkPostId));
    }

    // Get unapproved artwork post API
    @RequiresRole("Admin")
    @GetMapping("/GetUnapprovedArtworkPosts")
    public ResponseEntity<?> getUnapprovedArtworkPosts() {
        return ResponseEntity.ok(artworkPostServices.getUnapprovedArtworkPosts());
    }

    // Create artwork post API
    @RequiresRole("Artist")
    @PostMapping(value = "/CreateArtworkPost", consumes = "multipart/form-data")
    public ResponseEntity<?> createArtworkPost(
        @Valid @ModelAttribute ArtworkPostCreationDto artworkPostCreationDto
    ) throws IOException {
        // Create and check result
        boolean result = artworkPostServices.createArtworkPost(artworkPostCreationDto);
        if (!result)
            return ResponseEntity.internalServerError()
                    .body("An error occurred while creating the artwork post.");

        // Return success stats
        return ResponseEntity.ok("Artwork post created successfully.");
    }

    // Update artwork post API
    @RequiresRole("Artist")
    @PutMapping(value = "/UpdateArtworkPost", consumes = "multipart/form-data")
    public ResponseEntity<?> updateArtworkPost(
        @Valid @ModelAttribute ArtworkPostUpdatingDto artworkPostUpdatingDto
    ) throws IOException {
        // Update and check result
        boolean result = artworkPostServices.updateArtworkPost(artworkPostUpdatingDto);
        if (!result)
            return ResponseEntity.status(404)
                    .body("Could not update post. ID " + artworkPostUpdatingDto.getId() + " may not exist.");

        // Return success stats
        return ResponseEntity.ok("Artwork post updated successfully.");
    }

    // Delete artwork post API
    @RequiresRole({"Artist", "Admin"})
    @DeleteMapping("/DeleteArtworkPost")
    public ResponseEntity<?> deleteArtworkPost(@RequestParam Integer artworkPostId) {
        // Update and check result
        boolean result = artworkPostServices.deleteArtworkPost(artworkPostId);
        if (!result)
            return ResponseEntity.status(404)
                    .body("Could not delete post. ID " + artworkPostId + " may not exist.");

        // Return success stats
        return ResponseEntity.ok("Artwork post deleted successfully.");
    }

    // Change end date API
    @RequiresRole("Artist")
    @PatchMapping("/ChangeEndDate")
    public ResponseEntity<?> changeEndDate(
            @RequestParam Integer artworkPostId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        // Update and check result
        boolean result = artworkPostServices.updateEndDate(artworkPostId, endDate);
        if (!result)
            return ResponseEntity.badRequest().body("Failed to update the end date.");

        // Return success stats
        return ResponseEntity.ok("End date updated successfully.");
    }

    // Approve artwork post API
    @RequiresRole("Admin")
    @PatchMapping("/ApproveArtworkPost")
    public ResponseEntity<?> approveArtworkPost(
            @RequestParam Integer artworkPostId,
            @RequestParam Integer adminId
    ) {
        // Update and check result
        boolean result = artworkPostServices.markAsApproved(artworkPostId, adminId);
        if (!result)
            return ResponseEntity.badRequest()
                    .body("Approval failed. Please check the post and admin IDs.");

        // Return success stats
        return ResponseEntity.ok("Artwork post approved successfully.");
    }
}
