package com.artauction.auctionservice.Mappers.PostSold;

import com.artauction.auctionservice.Dtos.PostSold.UnpaidPostSoldDto;
import com.artauction.auctionservice.Entities.PostSold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnpaidPostSoldMapper {
    // Map Id
    @Mapping(target = "buyerId", expression = "java(postSold.getId().getBuyerId())")

    // NOTE: 'artworkPostTitle' belongs to the Artwork service
    @Mapping(target = "artworkPostTitle", ignore = true)
    UnpaidPostSoldDto toDto(PostSold postSold);
}
