package com.artauction.authenticationservices.Mappers;

import com.artauction.authenticationservices.Dtos.Authentication.AuthenticationDto;
import com.artauction.authenticationservices.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    // Map name
    @Mapping(source = "name", target = "name")

    // Ignore runtime properties (handled in service)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "expiresOn", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    AuthenticationDto toAuthenticationDto(User user);
}