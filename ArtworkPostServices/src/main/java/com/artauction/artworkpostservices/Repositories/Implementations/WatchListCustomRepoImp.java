package com.artauction.artworkpostservices.Repositories.Implementations;

import com.artauction.artworkpostservices.Config.IDs.WatchListId;
import com.artauction.artworkpostservices.Entities.WatchList;
import com.artauction.artworkpostservices.Repositories.Customs.WatchListCustomRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.ArtworkPostRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.WatchListRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WatchListCustomRepoImp implements WatchListCustomRepo {
    // Attributes
    private final WatchListRepo watchListRepo;
    private final ArtworkPostRepo artworkPostRepo;

    @Transactional
    @Override
    public boolean createWatchList(WatchList watchList) {
        // Check duplicate
        if (watchListRepo.existsById(watchList.getId()))
            return false;

        // Check artwork post id
        if (!artworkPostRepo.existsById(watchList.getId().getArtworkPostId()))
            return false;

        // Check artist existence

        // Create artwork post
        watchListRepo.save(watchList);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteWatchList(WatchListId watchListId) {
        // Check relation existence
        Optional<WatchList> wl = watchListRepo.findById(watchListId);
        if (wl.isEmpty())
            return false;

        // Delete watch list
        watchListRepo.delete(wl.get());
        return true;
    }
}
