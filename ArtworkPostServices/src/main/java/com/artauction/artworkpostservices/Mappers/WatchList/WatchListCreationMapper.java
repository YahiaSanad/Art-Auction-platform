package com.artauction.artworkpostservices.Mappers.WatchList;

import com.artauction.artworkpostservices.Dtos.WatchList.WatchListCreationDto;
import com.artauction.artworkpostservices.Entities.WatchList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WatchListCreationMapper {
    // Map DTO fields to the nested fields within the EmbeddedId 'id'
    @Mapping(target = "id.buyerId", source = "buyerId")
    @Mapping(target = "id.artworkPostId", source = "artworkPostId")

    // Ignore artwork post
    @Mapping(target = "artworkPost", ignore = true)
    WatchList toEntity(WatchListCreationDto dto);
}
