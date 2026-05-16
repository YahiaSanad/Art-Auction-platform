package com.artauction.artworkpostservices.Services.Implementations;

import com.artauction.artworkpostservices.Dtos.Artist.ArtistDto;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.*;
import com.artauction.artworkpostservices.Dtos.PostBid.PostBidDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;
import com.artauction.artworkpostservices.Kafka.Request.KafkaRequestReplyService;
import com.artauction.artworkpostservices.Mappers.ArtworkPost.*;
import com.artauction.artworkpostservices.Repositories.Customs.ArtworkPostCustomRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.ArtworkPostRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.TagRepo;
import com.artauction.artworkpostservices.Services.Interfaces.ArtworkPostServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtworkPostServicesImp implements ArtworkPostServices {
    // Attributes
    private final ArtworkPostRepo artworkPostRepo;
    private final ArtworkPostCustomRepo artworkPostCustomRepo;
    private final TagRepo tagRepo;
    private final ArtworkPostMapper artworkPostMapper;
    private final ArtistArtworkPostMapper artistArtworkPostMapper;
    private final ArtworkPostDetailedMapper artworkPostDetailedMapper;
    private final ArtworkPostCreationMapper artworkPostCreationMapper;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Methods
    @Override
    public List<ArtworkPostDto> getAllArtworkPosts() {
        // Get all artwork posts and map them
        List<ArtworkPost> artworkPosts = artworkPostRepo.findAllApprovedAndActive(LocalDateTime.now());

        // Get all artists
        return artworkPosts.stream()
            .map(artworkPost -> {
                // Get artworkPost
                ArtworkPostDto artworkPostDto = artworkPostMapper.toDto(artworkPost);

                try {
                    // Fetch artist data
                    ArtistDto artistDto = kafkaRequestReplyService.fetchArtistData(artworkPost.getArtistId());

                    // Check artist
                    if (artistDto != null){
                        artworkPostDto.setArtistName(artistDto.getName());
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }

                return artworkPostDto;
            }).toList();
    }

    @Override
    public List<ArtistArtworkPostDto> getAllArtistArtworkPosts(Integer artistId) {
        // Get artwork posts
        List<ArtworkPost> artworkPosts = artworkPostRepo.findAllByArtistIdAndActive(artistId, LocalDateTime.now());

        // Return mapped DTO
        return artistArtworkPostMapper.toDtoList(artworkPosts);
    }

    @Override
    public ArtworkPostDetailedDto getArtworkPostDetails(Integer artworkPostId) {
        // Get artwork post and map it
        Optional<ArtworkPost> artworkPost = artworkPostRepo.findApprovedAndActiveById(artworkPostId, LocalDateTime.now());
        ArtworkPostDetailedDto artworkPostDetailedDto = artworkPostDetailedMapper.toDto(artworkPost.orElse(null));

        // Get data for kafka
        try {
            // Get artist name and set it
            ArtistDto artistDto = kafkaRequestReplyService.fetchArtistData(artworkPost.get().getArtistId());
            if (artistDto != null)
                artworkPostDetailedDto.setArtistName(artistDto.getName());

            // Get post bids
            List<PostBidDto> postBidDtos = kafkaRequestReplyService.fetchBidsData(artworkPost.get().getId());
            if (!postBidDtos.isEmpty())
                artworkPostDetailedDto.setPostBids(postBidDtos);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return artworkPostDetailedDto;
    }

    @Override
    public List<ArtworkPostDto> getUnapprovedArtworkPosts() {
        // Get all unapproved artwork posts and map them
        List<ArtworkPost> unapprovedArtworkPosts = artworkPostRepo.findAllUnapproved();

        // Get all artists
        return unapprovedArtworkPosts.stream()
            .map(artworkPost -> {
                // Get artworkPost
                ArtworkPostDto artworkPostDto = artworkPostMapper.toDto(artworkPost);

                try {
                    // Fetch artist data
                    ArtistDto artistDto = kafkaRequestReplyService.fetchArtistData(artworkPost.getArtistId());

                    // Check artist
                    if (artistDto != null){
                        artworkPostDto.setArtistName(artistDto.getName());
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }

                return artworkPostDto;
            }).toList();
    }

    @Override
    public boolean createArtworkPost(ArtworkPostCreationDto artworkPostCreationDto) throws IOException {
        // Check artist existence
        try {
            if (!kafkaRequestReplyService.checkArtistExists(artworkPostCreationDto.getArtistId()))
                return false;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }

        // Map DTO to entity
        ArtworkPost artworkPost = artworkPostCreationMapper.toEntity(artworkPostCreationDto);

        // Map image manually
        if (artworkPostCreationDto.getImage() != null && !artworkPostCreationDto.getImage().isEmpty()) {
            artworkPost.setImage(artworkPostCreationDto.getImage().getBytes());
        }

        // Explicitly link each PostTag to this ArtworkPost
        if (artworkPost.getPostTags() != null) {
            artworkPost.getPostTags().forEach(pt -> {
                pt.setArtworkPost(artworkPost); // Sets the foreign key
                pt.setTag(tagRepo.getReferenceById(pt.getId().getTagId())); // Sets the composite key part
            });
        }

        // Save
        ArtworkPost saved = artworkPostCustomRepo.createArtworkPost(artworkPost);
        return true;
    }

    @Override
    public boolean updateArtworkPost(ArtworkPostUpdatingDto artworkPostUpdatingDto) throws IOException{
        // Map image manually
        byte[] imageBytes = (artworkPostUpdatingDto.getImage() != null && !artworkPostUpdatingDto.getImage().isEmpty())
                ? artworkPostUpdatingDto.getImage().getBytes() : null;

        // Update and return result
        return artworkPostCustomRepo.updateArtworkPost(artworkPostUpdatingDto, imageBytes);
    }

    @Override
    public boolean deleteArtworkPost(Integer artworkPostId) {
        return artworkPostCustomRepo.deleteArtworkPost(artworkPostId);
    }

    @Override
    public boolean updateEndDate(Integer artworkPostId, LocalDateTime endDate) {
        return artworkPostCustomRepo.updateEndDate(artworkPostId, endDate);
    }

    @Override
    public boolean markAsApproved(Integer artworkPostId, Integer adminId) {
        // Check admin existence
        try {
            if (!kafkaRequestReplyService.checkAdminExists(adminId))
                return false;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }

        return artworkPostCustomRepo.markAsApproved(artworkPostId, adminId);
    }
}
