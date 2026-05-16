package com.artauction.authenticationservices.Mappers;

import com.artauction.authenticationservices.Dtos.Authentication.RegisterArtistDto;
import com.artauction.authenticationservices.Entities.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterArtistMapper {
    // Map name and username
    @Mapping(source = "fullName", target = "name")

    // Ignore navigation properties
    @Mapping(target = "admin", ignore = true)
    Artist toArtist(RegisterArtistDto registerArtistDto);
}
