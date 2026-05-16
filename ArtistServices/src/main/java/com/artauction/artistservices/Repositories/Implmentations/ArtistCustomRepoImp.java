package com.artauction.artistservices.Repositories.Implmentations;

import com.artauction.artistservices.Entities.Artist;
import com.artauction.artistservices.Repositories.Customs.ArtistCustomRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Repository
public class ArtistCustomRepoImp implements ArtistCustomRepo {
    // Attributes
    @PersistenceContext
    private EntityManager entityManager;

    // Methods
    @Override
    @Transactional
    public boolean approveArtist(int artistId, int adminId) {
        // Check artist existence
        var artist = entityManager.find(Artist.class, artistId);
        if (artist == null || artist.getAdminId() != null)
            return false;

        // Update artist and return success
        artist.setAdminId(adminId);
        artist.setHireDate(LocalDateTime.now());
        entityManager.merge(artist);
        return true;
    }

    @Override
    @Transactional
    public boolean rejectArtist(int artistId) {
        // Check artist existence
        var artist = entityManager.find(Artist.class, artistId);
        if (artist == null)
            return false;

        // Remove artist
        entityManager.remove(artist);
        return false;
    }
}