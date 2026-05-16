package com.artauction.authenticationservices.Mappers;

import com.artauction.authenticationservices.Dtos.Authentication.RegisterBuyerDto;
import com.artauction.authenticationservices.Entities.Buyer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterBuyerMapper {
    // Map name and username
    @Mapping(source = "fullName", target = "name")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    Buyer toBuyer(RegisterBuyerDto registerBuyerDto);
}
