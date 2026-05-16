package com.artauction.artworkpostservices.Repositories.Customs;

import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostUpdatingDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;

import java.time.LocalDateTime;

public interface ArtworkPostCustomRepo {
    public ArtworkPost createArtworkPost(ArtworkPost artworkPost);
    public boolean updateArtworkPost(ArtworkPostUpdatingDto artworkPostUpdatingDto, byte[] image);
    public boolean deleteArtworkPost(Integer artworkPostId);
    public boolean updateEndDate(Integer artworkPostId, LocalDateTime endDate);
    public boolean markAsApproved(Integer artworkPostId, Integer adminId);
}
