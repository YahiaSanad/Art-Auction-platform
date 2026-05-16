package com.artauction.auctionservice.Mappers.PostBid;

import com.artauction.auctionservice.Dtos.PostBid.PostBidCreationDto;
import com.artauction.auctionservice.Entities.PostBid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface PostBidCreationMapper {
    // Map creationDto → entity
    @Mapping(target = "id.buyerId", source = "buyerId")
    @Mapping(target = "id.artworkPostId", source = "artworkPostId")
    @Mapping(target = "bidTime", expression = "java(LocalDateTime.now())")
    PostBid toEntity(PostBidCreationDto postBidCreationDto);
}
