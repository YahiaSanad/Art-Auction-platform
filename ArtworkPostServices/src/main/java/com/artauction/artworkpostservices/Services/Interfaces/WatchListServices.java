package com.artauction.artworkpostservices.Services.Interfaces;

import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.artworkpostservices.Dtos.WatchList.WatchListCreationDto;

import java.util.List;

public interface WatchListServices {
    public List<ArtworkPostDto> getWatchListForBuyer(Integer buyerId);
    public boolean createWatchList(WatchListCreationDto watchListCreationDto);
    public boolean deleteWatchList(Integer buyerId, Integer artworkPostId);
}
