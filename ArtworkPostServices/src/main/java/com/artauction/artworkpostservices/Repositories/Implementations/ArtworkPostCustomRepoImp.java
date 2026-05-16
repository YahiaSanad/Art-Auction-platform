package com.artauction.artworkpostservices.Repositories.Implementations;

import com.artauction.artworkpostservices.Config.IDs.PostTagId;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostUpdatingDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;
import com.artauction.artworkpostservices.Entities.PostTag;
import com.artauction.artworkpostservices.Repositories.Customs.ArtworkPostCustomRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.ArtworkPostRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.CategoryRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.TagRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArtworkPostCustomRepoImp implements ArtworkPostCustomRepo {
    // Attributes
    private final ArtworkPostRepo artworkPostRepo;
    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;

    // Methods

    // Equivalent to CreateArtworkPost(ArtworkPost artworkPost)
    @Transactional
    @Override
    public ArtworkPost createArtworkPost(ArtworkPost artworkPost) {
        // Note: verify artistId exists

        // Return status of saving
        return artworkPostRepo.save(artworkPost);
    }

    // Equivalent to UpdateArtworkPost(ArtworkPost artworkPost)
    @Transactional
    @Override
    public boolean updateArtworkPost(ArtworkPostUpdatingDto artworkPostUpdatingDto, byte[] image) {
        // Check artwork post existence
        Optional<ArtworkPost> existing = artworkPostRepo.findByIdWithPostTags(artworkPostUpdatingDto.getId());
        if (existing.isEmpty())
            return false;
        ArtworkPost artpost = existing.get();

        // Update main field
        artpost.setTitle(artworkPostUpdatingDto.getTitle());
        artpost.setDescription(artworkPostUpdatingDto.getDescription());
        artpost.setInitialPrice(artworkPostUpdatingDto.getInitialPrice());
        artpost.setStartDate(artworkPostUpdatingDto.getStartDate());
        artpost.setEndDate(artworkPostUpdatingDto.getEndDate());
        artpost.setCategory(categoryRepo.getReferenceById(artworkPostUpdatingDto.getCategoryId()));
        if (artworkPostUpdatingDto.getImage() != null) {
            artpost.setImage(image);
        }

        // Clear past tags and re-add
        artpost.getPostTags().clear();
        if (artworkPostUpdatingDto.getTagIds() != null) {
            for (Integer tagId : artworkPostUpdatingDto.getTagIds()) {
                PostTag newPostTag = new PostTag();
                PostTagId ptId = new PostTagId();
                ptId.setArtworkPostId(artpost.getId());
                ptId.setTagId(tagId);

                newPostTag.setId(ptId);
                newPostTag.setArtworkPost(artpost); // Links to the parent
                newPostTag.setTag(tagRepo.getReferenceById(tagId)); // Links to the tag proxy

                artpost.getPostTags().add(newPostTag);
            }
        }

        // Save data
        artworkPostRepo.save(artpost);
        return true;
    }

    // Equivalent to DeleteArtworkPost(int artworkPostId)
    @Transactional
    @Override
    public boolean deleteArtworkPost(Integer artworkPostId) {
        // Check artwork post existence
        if (!artworkPostRepo.existsById(artworkPostId))
            return false;

        // Delete artwork post
        artworkPostRepo.deleteById(artworkPostId);
        return true;
    }

    // Equivalent to UpdateEndDate(int artworkPostId, DateTime endDate)
    @Transactional
    @Override
    public boolean updateEndDate(Integer artworkPostId, LocalDateTime endDate) {
        // Check artwork post existence
        Optional<ArtworkPost> artworkPost = artworkPostRepo.findById(artworkPostId);
        if (artworkPost.isEmpty())
            return false;

        // Update and save
        artworkPost.get().setEndDate(endDate);
        artworkPostRepo.save(artworkPost.get());
        return true;
    }

    // Equivalent to MarkAsApproved(int artworkPostId, int adminId)
    @Transactional
    @Override
    public boolean markAsApproved(Integer artworkPostId, Integer adminId) {
        // Check artwork post existence
        Optional<ArtworkPost> artworkPost = artworkPostRepo.findById(artworkPostId);
        if (artworkPost.isEmpty())
            return false;

        // Note: verify adminId exists

        // Update and save
        artworkPost.get().setAdminId(adminId);
        artworkPostRepo.save(artworkPost.get());
        return true;
    }
}
