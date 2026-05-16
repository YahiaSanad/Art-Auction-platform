package com.artauction.artworkpostservices.Mappers.ArtworkPost;

import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtworkPostMapper {
    // Map image, category name and tags
    @Mapping(target = "image",
        expression = "java(toBase64(artworkPost.getImage()))")
    @Mapping(target = "categoryName",
        expression = "java(artworkPost.getCategory() != null ? artworkPost.getCategory().getName() : \"\")")
    @Mapping(target = "tags",
        expression = "java(artworkPost.getPostTags().stream()" +
            ".map(pt -> pt.getTag().getName()).toArray(String[]::new))")
    @Mapping(target = "buyNowPrice", source = "buyNowPrice")

    // Ignore cross-service attributes
    @Mapping(target = "artistName", ignore = true)
    ArtworkPostDto toDto(ArtworkPost artworkPost);
    List<ArtworkPostDto> toDtoList(List<ArtworkPost> artworkPosts);

    // Convert byte[] -> base64 string
    default String toBase64(byte[] image) {
        return (image != null && image.length > 0) ? Base64.getEncoder().encodeToString(image) : "";
    }
}
