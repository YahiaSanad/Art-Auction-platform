package com.artauction.auctionservice.Services.Interfaces;

import com.artauction.auctionservice.Dtos.PostBid.BuyerPostBidDto;
import com.artauction.auctionservice.Dtos.PostBid.PostBidCreationDto;
import com.artauction.auctionservice.Dtos.PostBid.PostBidDto;
import com.artauction.auctionservice.Entities.PostSold;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostBidServices {
    List<PostBidDto> getAllPostBids(int artworkPostId);
    Collection<BuyerPostBidDto> getAllBuyerBids(int buyerId);
    boolean createPostBid(PostBidCreationDto postBidCreationDto);
    boolean deletePostBid(int artworkPostId, int buyerId);
}
