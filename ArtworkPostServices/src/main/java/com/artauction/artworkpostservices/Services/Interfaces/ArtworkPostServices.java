package com.artauction.artworkpostservices.Services.Interfaces;

import com.artauction.artworkpostservices.Dtos.ArtworkPost.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtworkPostServices {
    public List<ArtworkPostDto> getAllArtworkPosts();
    public List<ArtistArtworkPostDto> getAllArtistArtworkPosts(Integer artistId);
    public ArtworkPostDetailedDto getArtworkPostDetails(Integer artworkPostId);
    public List<ArtworkPostDto> getUnapprovedArtworkPosts();
    public boolean createArtworkPost(ArtworkPostCreationDto artworkPostCreationDto) throws IOException;
    public boolean updateArtworkPost(ArtworkPostUpdatingDto artworkPostUpdatingDto) throws IOException;
    public boolean deleteArtworkPost(Integer artworkPostId);
    public boolean updateEndDate(Integer artworkPostId, LocalDateTime endDate);
    public boolean markAsApproved(Integer artworkPostId, Integer adminId);
}
