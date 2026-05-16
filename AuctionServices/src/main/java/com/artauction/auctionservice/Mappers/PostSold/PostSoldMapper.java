package com.artauction.auctionservice.Mappers.PostSold;

import com.artauction.auctionservice.Dtos.PostSold.PostSoldDto;
import com.artauction.auctionservice.Entities.PostSold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostSoldMapper {
    // Note: buyerName → enriched from AuthenticationServices
    @Mapping(target = "buyerName", ignore = true)
    PostSoldDto toDto(PostSold postSold);
    List<PostSoldDto> toDtoList(List<PostSold> postSolds);
}
