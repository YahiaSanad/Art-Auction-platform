package com.artauction.auctionservice.Mappers.PostSold;

import com.artauction.auctionservice.Dtos.PostSold.BuyerPostSoldDto;
import com.artauction.auctionservice.Entities.PostSold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuyerPostSoldMapper {
    // NOTE: 'title' belongs to the Artwork service
    @Mapping(target = "title", ignore = true)
    BuyerPostSoldDto toDto(PostSold postSold);
}
