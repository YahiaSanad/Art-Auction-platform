package com.artauction.auctionservice.Mappers.PostBid;

import com.artauction.auctionservice.Dtos.PostBid.PostBidDto;
import com.artauction.auctionservice.Entities.PostBid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostBidMapper {
    // Note: buyerName → enriched from AuthenticationServices
    @Mapping(target = "buyerName", ignore = true)
    PostBidDto toDto(PostBid postBid);
    List<PostBidDto> toDtoList(List<PostBid> postBids);
}
