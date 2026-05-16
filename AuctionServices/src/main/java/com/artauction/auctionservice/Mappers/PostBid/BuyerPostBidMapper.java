package com.artauction.auctionservice.Mappers.PostBid;

import com.artauction.auctionservice.Dtos.PostBid.BuyerPostBidDto;
import com.artauction.auctionservice.Entities.PostBid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuyerPostBidMapper {
    @Mapping(target = "artworkPostId", expression = "java(postBid.getId().getArtworkPostId())")
    @Mapping(target = "bidTime", source = "bidTime")

    // NOTE: 'title' belongs to the Artwork service
    @Mapping(target = "title", ignore = true)
    BuyerPostBidDto toDto(PostBid postBid);
}
