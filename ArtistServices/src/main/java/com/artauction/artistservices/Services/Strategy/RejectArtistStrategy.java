package com.artauction.artistservices.Services.Strategy;

import com.artauction.artistservices.Repositories.Interfaces.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RejectArtistStrategy implements ArtistProcessStrategy {
    // Attributes
    private final ArtistRepo artistRepo;

    // Methods
    @Override
    public boolean execute(int artistId, Integer adminId) {
        // Check artist existence
        if (!artistRepo.existsById(artistId)) {
            return false;
        }

        // Delete artist
        artistRepo.deleteById(artistId);
        return true;
    }
}