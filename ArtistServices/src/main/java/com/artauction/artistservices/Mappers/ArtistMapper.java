package com.artauction.artistservices.Mappers;

import com.artauction.artistservices.Dtos.ArtistDto;
import com.artauction.artistservices.Entities.Artist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    ArtistDto toDto(Artist artist);
}