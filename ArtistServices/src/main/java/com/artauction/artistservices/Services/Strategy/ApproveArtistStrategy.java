package com.artauction.artistservices.Services.Strategy;

import com.artauction.artistservices.Entities.Artist;
import com.artauction.artistservices.Kafka.Request.KafkaRequestReplyService;
import com.artauction.artistservices.Repositories.Interfaces.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApproveArtistStrategy implements ArtistProcessStrategy {
    // Attibutes
    private final ArtistRepo artistRepo;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Methods
    @Override
    public boolean execute(int artistId, Integer adminId) {
        // Get artist
        Optional<Artist> artistOpt = artistRepo.findById(artistId);
        if (artistOpt.isEmpty()) return false;

        // Check admin existence
        try {
            if (!kafkaRequestReplyService.checkAdminExists(adminId)) {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Kafka Communication Error: " + e.getMessage());
            return false;
        }

        // Approve artist
        Artist artist = artistOpt.get();
        artist.setAdminId(adminId);
        artist.setHireDate(LocalDateTime.now());
        artistRepo.save(artist);
        return true;
    }
}