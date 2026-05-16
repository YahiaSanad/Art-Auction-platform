package com.artauction.artistservices.Controllers;

import com.artauction.artistservices.Aops.Roles.RequiresRole;
import com.artauction.artistservices.Dtos.ArtistDto;
import com.artauction.artistservices.Services.Interfaces.ArtistServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Artist")
@RequiredArgsConstructor
@RequiresRole("Admin")
public class ArtistController {
    // Attributes
    private final ArtistServices artistServices;

    // Get unapproved artists API
    @GetMapping("/GetUnapprovedArtists")
    public ResponseEntity<List<ArtistDto>> getUnapprovedArtists() {
        return ResponseEntity.ok(artistServices.getUnapprovedArtists());
    }

    // Get approved artists API
    @GetMapping("/GetApprovedArtists")
    public ResponseEntity<List<ArtistDto>> getApprovedArtists() {
        return ResponseEntity.ok(artistServices.getApprovedArtists());
    }

    // Approve artist API
    @PatchMapping("/ApproveArtist")
    public ResponseEntity<?> approveArtist(@RequestParam int artistId, @RequestParam int adminId) {
        // Try to approve and check result
        boolean result = artistServices.approveArtist(artistId, adminId);
        if (!result) {
            return ResponseEntity.status(404).body("Artist " + artistId + " not found or already approved");
        }

        // Return success message
        return ResponseEntity.ok("Artist successfully approved");
    }

    // Reject artist API
    @PatchMapping("/RejectArtist")
    public ResponseEntity<?> rejectArtist(@RequestParam int artistId) {
        // Try to reject and check result
        boolean result = artistServices.rejectArtist(artistId);
        if (!result) {
            return ResponseEntity.status(404).body("Artist " + artistId + " not found");
        }

        // Return success message
        return ResponseEntity.ok("Artist successfully rejected");
    }
}