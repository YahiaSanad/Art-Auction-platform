package com.artauction.artworkpostservices.Mappers.ArtworkPost;

import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostDetailedDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtworkPostDetailedMapper {
    // Map image, category name and tags
    @Mapping(target = "image",
        expression = "java(toBase64(artworkPost.getImage()))")
    @Mapping(target = "categoryName",
        expression = "java(artworkPost.getCategory() != null ? artworkPost.getCategory().getName() : \"\")")
    @Mapping(target = "tags",
        expression = "java(artworkPost.getPostTags() != null ? artworkPost.getPostTags()" +
            ".stream().map(pt -> pt.getTag().getName()).toArray(String[]::new) : new String[0])")
    @Mapping(target = "buyNowPrice", source = "buyNowPrice")

    // Ignore other attributes (cross-service)
    @Mapping(target = "postBids", ignore = true)
    @Mapping(target = "artistName", ignore = true)
    ArtworkPostDetailedDto toDto(ArtworkPost artworkPost);
    List<ArtworkPostDetailedDto> toDtoList(List<ArtworkPost> artworkPosts);

    // Convert byte[] → Base64 string
    default String toBase64(byte[] image) {
        return (image != null && image.length > 0) ? Base64.getEncoder().encodeToString(image) : null;
    }
}
