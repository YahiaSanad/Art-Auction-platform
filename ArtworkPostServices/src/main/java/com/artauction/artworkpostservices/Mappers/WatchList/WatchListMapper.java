package com.artauction.artworkpostservices.Mappers.WatchList;


import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.artworkpostservices.Entities.WatchList;
import com.artauction.artworkpostservices.Mappers.ArtworkPost.ArtworkPostMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtworkPostMapper.class})
public interface WatchListMapper {
    // Map image, category name and tags
    @Mapping(target = "id", source = "artworkPost.id")
    @Mapping(target = "title", source = "artworkPost.title")
    @Mapping(target = "description", source = "artworkPost.description")
    @Mapping(target = "initialPrice", source = "artworkPost.initialPrice")
    @Mapping(target = "buyNowPrice", source = "artworkPost.buyNowPrice")
    @Mapping(target = "startDate", source = "artworkPost.startDate")
    @Mapping(target = "endDate", source = "artworkPost.endDate")
    @Mapping(target = "image", expression = "java(toBase64(watchList.getArtworkPost().getImage()))")
    @Mapping(target = "categoryName", expression = "java(watchList.getArtworkPost().getCategory() != null ? " +
        "watchList.getArtworkPost().getCategory().getName() : \"\")")
    @Mapping(target = "tags", expression = "java(watchList.getArtworkPost().getPostTags()." +
        "stream().map(pt -> pt.getTag().getName()).toArray(String[]::new))")

    // Ignore cross-service
    @Mapping(target = "artistName", ignore = true)
    ArtworkPostDto toArtworkPostDto(WatchList watchList);
    List<ArtworkPostDto> toArtworkPostDtoList(List<WatchList> watchLists);

    // Convert byte[] -> base64 string
    default String toBase64(byte[] image) {
        return (image != null && image.length > 0) ? Base64.getEncoder().encodeToString(image) : "";
    }
}
